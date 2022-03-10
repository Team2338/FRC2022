package team.gif.lib.logging;

import edu.wpi.first.wpilibj.Timer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Writes a list of metrics to a file in CSV format. Each line is timestamped with the time since this FileLogger was initialized.
 * The consumer must call {@link FileLogger#init()} once to initialize this logger, and then {@link FileLogger#run()} each control
 * loop to log the desired metrics to a file. It is recommended to call <code>run()</code> in the <code>robotPeriodic()</code> function.
 *
 * @author Patrick Ubelhor
 * @since 3/9/2022
 */
public class FileLogger {
	
	// Flush every 5 seconds (20ms per iteration * 250 iterations)
	private static final int ITERATIONS_BETWEEN_FLUSHES = 250;
	
	private final FileWriter fw;
	private final LinkedList<String> names = new LinkedList<>();
	private final LinkedList<Supplier<String>> suppliers = new LinkedList<>();
	private double initTime = 0;
	private int counter = 0;
	
	/**
	 * Constructs a <code>FileLogger</code>.
	 *
	 * @throws IOException If the log file already exists but is a directory rather than a regular file,
	 * does not exist but cannot be created, or cannot be opened for any other reason
	 */
	public FileLogger() throws IOException {
		fw = new FileWriter("/logs/ViperLog.csv");
		addMetric("Time", () -> Timer.getFPGATimestamp() - initTime);
	}
	
	/**
	 * Adds a metric to the list of values to be logged. Doubles are formatted to two decimal places.
	 *
	 * @param name The name of the metric
	 * @param supplier A function to retrieve the metric value
	 */
	public void addMetric(String name, DoubleSupplier supplier) {
		addMetric(name, () -> String.format("%.2f", supplier.getAsDouble()));
	}
	
	/**
	 * Adds a metric to the list of values to be logged.
	 *
	 * @param name The name of the metric
	 * @param supplier A function to retrieve the metric value
	 */
	public void addMetric(String name, Supplier<String> supplier) {
		names.addLast(name);
		suppliers.addLast(supplier);
	}

    public void addEvent(String label) throws IOException {
        fw.append(label).append("\n");
    }
	
	/**
	 * Initializes this FileLogger. MUST be called before {@link FileLogger#run()}.
	 *
	 * @throws IOException If an IO error occurs
	 */
	public void init() throws IOException {
		initTime = Timer.getFPGATimestamp();
		String header = String.join(",", names);
		fw.append(header).append("\n");
	}
	
	/**
	 * Prints all the specified metrics to a file.
	 * <p>
	 * Note that this is not guaranteed to immediately print to a file. When this
	 * is called, the line is first put into a buffer. To guarantee the buffer gets
	 * flushed to the file, call {@link FileLogger#flush()}.
	 * <p>
	 * If the robot shuts down before the buffer is flushed, any contents still
	 * in the buffer will be lost. However, flushing the buffer is a <i>potentially</i>
	 * expensive action, and should not be done on every control loop. The buffer
	 * may flush itself at any time.
	 *
	 * @throws IOException If an IO error occurs
	 */
	public void run() throws IOException {
		List<String> values = suppliers.stream()
				.map(Supplier::get)
				.collect(Collectors.toList());
		String line = String.join(",", values);
		fw.append(line).append("\n");
		
		counter++;
		if (counter == ITERATIONS_BETWEEN_FLUSHES) {
			flush();
			counter = 0;
		}
	}
	
	/**
	 * Flushes the contents of the internal buffer to the file.
	 * <p>
	 * If the robot shuts down before the buffer is flushed, any contents still
	 * in the buffer will be lost. However, flushing the buffer is a <i>potentially</i>
	 * expensive action, and should not be done on every control loop. The buffer
	 * may flush itself at any time.
	 *
	 * @throws IOException If an IO error occurs
	 */
	public void flush() throws IOException {
		fw.flush();
	}
	
	/**
	 * Closes the logger, flushing it first. Once the logger has been closed,
	 * further {@link FileLogger#run()} or {@link FileLogger#flush()} invocations will
	 * cause an IOException to be thrown. Closing a previously closed logger has no effect.
	 *
	 * @throws IOException
	 */
	public void close() throws IOException {
		fw.close();
	}
	
}
