package ddc.support.util;

import org.apache.commons.lang3.ArrayUtils;

public class ClassUtil {

	public static Boolean implementsInterface(Object object, Class interf){
	    return ArrayUtils.contains(object.getClass().getInterfaces(), interf);
	}

}
