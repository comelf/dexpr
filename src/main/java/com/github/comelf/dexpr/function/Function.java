package com.github.comelf.dexpr.function;

import java.util.List;

public interface Function {
	Object process(List param);
	String desc();
}
