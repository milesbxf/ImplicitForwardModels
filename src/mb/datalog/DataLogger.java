package mb.datalog;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * Logs data from a specified class with the Loggable interface and writes it to
 * a CSV file.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public final class DataLogger {
	
	private static final Logger			LOGGER	= Logger.getLogger( "DataLogger" );
	
	private final Loggable				loggable;
	private final List<List<Object>>	data;
	
	private final int					numberCols;
	
	/**
	 * Initialises this DataLogger with a Loggable object.
	 * 
	 * @param loggable
	 */
	public DataLogger ( Loggable loggable ) {
		numberCols = loggable.getHeaders().size();
		data = new LinkedList<>();
		this.loggable = loggable;
	}
	
	/**
	 * @return the data currently logged by this DataLogger.
	 */
	public List<List<Object>> getData() {
		return data;
	}
	
	/**
	 * @return the headers specified by the Loggable.
	 */
	public List<String> getHeaders() {
		return loggable.getHeaders();
	}
	
	/**
	 * Requests data from the Loggable and adds it to this object's data.
	 * Normally called during step() or update() functionson each timestep/GA generation.
	 */
	public void appendData() {
		List<Object> dataToAdd = loggable.getData();
		if ( dataToAdd.size() != numberCols )
			throw new IllegalArgumentException( String.format( "Expected %d columns of data; got %d", numberCols, dataToAdd.size() ) );
		data.add( dataToAdd );
	}
	
	/**
	 * Writes all the data in this DataLogger to the file specified as CSV.
	 * @param file
	 * @throws IOException
	 */
	public void writeToCSVFile( File file ) throws IOException {
		CSVWriter.CSVParamBuilder builder = new CSVWriter.CSVParamBuilder();
		builder.setHeaderNames( loggable.getHeaders() );
		builder.setData( data );
		CSVWriter.writeToCSVFile( file, builder );
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder( "|" );
		
		for ( String header : getHeaders() ) {
			str.append( String.format( "%10s", header ) ).append( "\t|" );
		}
		
		str.append( "\n" );
		
		for ( List<Object> data : getData() ) {
			str.append( "|" );
			for ( Object datum : data ) {
				str.append( datum ).append( "\t|" );
			}
			str.append( "\n" );
		}
		
		return str.toString();
	}
	
}
