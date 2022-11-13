package io.github.ritonglue.gocsv.convert;

/**
 * <p>
 * Converter is an interface
 * describing a Java class that can perform Object-to-String and String-to-Object conversions between model data objects
 * and a String representation of those objects.
 * </p>
 *
 * @param <T> The generic type of object value to convert.
 */

public interface Converter<T> {

    /**
     * Convert the specified string value.
     * @param value String value to be converted (may be <code>null</code>)
     * @return <code>null</code> if the value to convert is <code>null</code>, otherwise the result of the conversion
     */
    T getAsObject(String value);

    /**
     * Convert the specified model object value.
     * @param value Model object value to be converted (may be <code>null</code>)
     * @return a zero-length String if value is <code>null</code>, otherwise the result of the conversion
     */
    String getAsString(T value);
}

