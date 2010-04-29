package org.openengsb.ekb.api;


public interface EKB {

    String transformMethodCall(String senderQname, String receiverQName, String xml);

    String transformReturnValue(String senderQname, String receiverQName, String xml);

    String transformEvent(String senderQname, String receiverQName, String xml);

}
