package team.gif.lib.logging;

import edu.wpi.first.wpilibj.Timer;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.Flushable;
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
public class FileLogger implements Closeable, Flushable {
	
	// Flush every 5 seconds (20ms per iteration * 250 iterations)
	private static final int ITERATIONS_BETWEEN_FLUSHES = 250;
	private static final String LOG_DIR = "/logs";
	private static final String EVENT_LOG_PREFIX = "events";
	private static final String FILE_EXTENSION = ".csv";
	
	private final FileWriter fw;
	private final FileWriter eventFileWriter;
	private final LinkedList<String> names = new LinkedList<>();
	private final LinkedList<Supplier<String>> suppliers = new LinkedList<>();
	private double initTime = 0;
	private int counter = 0;
	
	/**
	 * Constructs a <code>FileLogger</code>.
	 */
	public FileLogger() {
		int nextFileNumber = getNextFileNumber();
		
		String nextEventFileName = EVENT_LOG_PREFIX + nextFileNumber + FILE_EXTENSION;
		this.eventFileWriter = createFileWriter(nextEventFileName);
		
		this.fw = createFileWriter("ViperLog.csv");
		
		addMetric("Time", this::getCurrentTime);
	}
	
	private int getNextFileNumber() {
		File logDir = new File(LOG_DIR);
		String[] lognames = logDir.list((File dir, String name) -> name.startsWith(EVENT_LOG_PREFIX));
		
		if (lognames == null) {
			System.err.println("Failed to read log directory");
			return 0;
		}
		
		int max = 0;
		for (String logname : lognames) {
			String rawNumber = logname.substring(EVENT_LOG_PREFIX.length());
			int number = Integer.parseInt(rawNumber);
			
			max = Math.max(max, number);
		}
		
		return max;
	}
	
	private FileWriter createFileWriter(String filename) {
		FileWriter fw;
		try {
			fw = new FileWriter(LOG_DIR + "/" + filename);
		} catch (IOException e) {
			System.err.println("Failed to create " + filename);
			System.err.println(e.getMessage());
			e.printStackTrace();
			
			fw = null;
		}
		
		return fw;
	}
	
	private double getCurrentTime() {
		return Timer.getFPGATimestamp() - initTime;
	}
	
	private String formatDouble(double number) {
		return String.format("%.2f", number);
	}
	
	public void addEvent(String mode, String event) {
		if (eventFileWriter == null) {
			return;
		}
		
		String line = String.format("[%s] %s - %s", mode, getCurrentTime(), event);
		
		try {
			eventFileWriter.append(line).append("\n");
		} catch (IOException e) {
			System.err.println("Failed to run event logger");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a metric to the list of values to be logged. Doubles are formatted to two decimal places.
	 *
	 * @param name The name of the metric
	 * @param supplier A function to retrieve the metric value
	 */
	public void addMetric(String name, DoubleSupplier supplier) {
		addMetric(name, () -> this.formatDouble(supplier.getAsDouble()));
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
	
	/**
	 * Initializes this FileLogger. MUST be called before {@link FileLogger#run()}.
	 */
	public void init() {
		if (fw == null) {
			return;
		}
		
		initTime = Timer.getFPGATimestamp();
		counter = 0;
		String header = String.join(",", names);
		
		try {
			fw.append(header).append("\n");
		} catch (IOException e) {
			System.err.println("Failed to initialize logger");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
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
	 */
	public void run() {
		if (fw == null) {
			return;
		}
		
		List<String> values = suppliers.stream()
				.map(Supplier::get)
				.collect(Collectors.toList());
		String line = String.join(",", values);
		
		try {
			fw.append(line).append("\n");
			
			counter++;
			if (counter == ITERATIONS_BETWEEN_FLUSHES) {
				flush();
				counter = 0;
			}
		} catch (IOException e) {
			System.err.println("Failed to run logger");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Flushes the contents of the internal buffer to the file.
	 * <p>
	 * If the robot shuts down before the buffer is flushed, any contents still
	 * in the buffer will be lost. However, flushing the buffer is a <i>potentially</i>
	 * expensive action, and should not be done on every control loop. The buffer
	 * may flush itself at any time.
	 */
	@Override
	public void flush() {
		if (fw == null) {
			return;
		}
		
		try {
			fw.flush();
		} catch (IOException e) {
			System.err.println("Failed to flush logger");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the logger, flushing it first. Once the logger has been closed,
	 * further {@link FileLogger#run()} or {@link FileLogger#flush()} invocations will
	 * cause an IOException to be thrown. Closing a previously closed logger has no effect.
	 */
	@Override
	public void close() {
		if (fw == null) {
			return;
		}
		
		try {
			fw.close();
		} catch (IOException e) {
			System.err.println("Failed to close logger");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
}
