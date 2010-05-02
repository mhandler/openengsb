package org.openengsb.ekb.api;

public interface EKB {

    String transformMethodCall(String sender, String receiver, String xml);

    String transformReturnValue(String sender, String receiver, String xml);

    String transformEvent(String sender, String receiver, String xml);

}
