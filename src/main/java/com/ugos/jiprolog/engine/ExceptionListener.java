package com.ugos.jiprolog.engine;

public interface ExceptionListener {
//	public WAM.Node notifyException(JIPRuntimeException ex);
boolean notifyException(JIPRuntimeException ex);
}
