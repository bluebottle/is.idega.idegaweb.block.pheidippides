package is.idega.idegaweb.pheidippides.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("pheidippidesUtil")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class PheidippidesUtil {

	public static String concat(String arg1, String arg2) {
		return arg1.concat(arg2);
	}
	
}