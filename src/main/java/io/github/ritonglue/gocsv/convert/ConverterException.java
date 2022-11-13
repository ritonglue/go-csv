package io.github.ritonglue.gocsv.convert;

/**
 * <p>
 * <strong>ConverterException</strong> is an exception thrown by the <code>getAsObject()</code> or
 * <code>getAsString()</code> method of a {@link Converter}, to indicate that the requested conversion cannot be
 * performed.
 * </p>
 */

public class ConverterException extends RuntimeException {

	private static final long serialVersionUID = 1;

	/**
	 * <p>
	 * Construct a new exception with no detail message or root cause.
	 * </p>
	 */
	public ConverterException() {
		super();
	}

	/**
	 * <p>
	 * Construct a new exception with the specified detail message and no root cause.
	 * </p>
	 *
	 * @param message The detail message for this exception
	 */
	public ConverterException(String message) {
		super(message);
	}

	/**
	 * <p>
	 * Construct a new exception with the specified root cause. The detail message will be set to
	 * <code>(cause == null ? null :
	 * cause.toString()</code>
	 *
	 * @param cause The root cause for this exception
	 */
	public ConverterException(Throwable cause) {
		super(cause);
	}

	/**
	 * <p>
	 * Construct a new exception with the specified detail message and root cause.
	 * </p>
	 *
	 * @param message The detail message for this exception
	 * @param cause The root cause for this exception
	 */
	public ConverterException(String message, Throwable cause) {
		super(message, cause);
	}
}
