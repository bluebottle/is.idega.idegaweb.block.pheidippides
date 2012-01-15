package is.idega.idegaweb.pheidippides.util;

import is.idega.idegaweb.pheidippides.business.ShirtSizeGender;
import is.idega.idegaweb.pheidippides.business.ShirtSizeSizes;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.util.IWTimestamp;

@Service("pheidippidesUtil")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class PheidippidesUtil {

	public static String concat(String arg1, String arg2) {
		return arg1.concat(arg2);
	}

	public static List<ShirtSizeSizes> getSizes() {
		return Arrays.asList(ShirtSizeSizes.values());
	}

	public static List<ShirtSizeGender> getGenders() {
		return Arrays.asList(ShirtSizeGender.values());
	}

	public static String formatDate(Date date) {
		if (date != null) {
			return new IWTimestamp(date).getDateString("d.M.yyyy H:mm");
		}
		return "";
	}
}