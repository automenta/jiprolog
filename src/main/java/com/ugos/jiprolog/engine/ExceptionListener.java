package com.ugos.jiprolog.engine;

interface ExceptionListener {
//	public WAM.Node notifyException(JIPRuntimeException ex);
boolean notifyException(JIPRuntimeException ex);
}
