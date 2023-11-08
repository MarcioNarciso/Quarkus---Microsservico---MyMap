package br.dev.marcionarciso.utils;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	
	public static Boolean isEmpty(String string) {
		return StringUtils.isBlank(string)
				|| StringUtils.isEmpty(string)
				|| Objects.isNull(string);
	}

}
