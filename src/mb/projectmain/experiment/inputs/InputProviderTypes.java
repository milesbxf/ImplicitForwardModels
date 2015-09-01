package mb.projectmain.experiment.inputs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Provides an enumeration with all available InputProvider types. A new
 * instance of a particular type can be dynamically created.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public enum InputProviderTypes {
	
	FLAT_SINE( FlatSineInputProvider.class, Lists.newArrayList( "Frequency", "Amplitude" ) ),
	SINUSOIDAL( SinusoidalInputProvider.class, Lists.newArrayList( "Frequency multiplier", "Amplitude multiplier" ) ),
	SQUARE( SquareWaveInputProvider.class, Lists.newArrayList( "Frequency multiplier", "Amplitude multiplier" ) ),
	SAWTOOTH( SawtoothInputProvider.class, Lists.newArrayList( "Frequency multiplier", "Amplitude multiplier" ) ),
	TRIANGLE( TriangleInputProvider.class, Lists.newArrayList( "Frequency multiplier", "Amplitude multiplier" ) );
	
	/**
	 * @param clazz
	 */
	private InputProviderTypes ( Class<? extends InputProvider> clazz, List<String> parameters ) {
		this.clazz = clazz;
		this.parameters = parameters;
	}
	
	private final Class<? extends InputProvider>	clazz;
	private final List<String>						parameters;
	
	/**
	 * Creates a new instance of this InputProvider type.
	 * 
	 * @param parameters
	 *            Parameters (normally float array) to pass to the
	 *            InputProvider.
	 * @return a new InputProvider
	 */
	public InputProvider createNewInstance( Object... parameters ) {
		for ( Constructor<?> constructor : clazz.getConstructors() ) {
			if ( constructor.getParameterTypes().length == parameters.length ) {
				try {
					return (InputProvider) constructor.newInstance( parameters );
				} catch ( InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
					throw new RuntimeException( "Error instantiating " + clazz.getName(), e );
				}
			}
		}
		throw new RuntimeException( "Could not instantiate " + clazz.getName() + "; parameters provided did not match a constructor" );
	}
	
	/**
	 * @return a list of String descriptors with parameters
	 */
	public List<String> getParameters() {
		return parameters;
	}
	
}
