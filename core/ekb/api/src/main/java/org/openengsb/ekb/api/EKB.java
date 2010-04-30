package org.openengsb.ekb.api;

public interface EKB {

    /**
     * Transform the xml of a method call message for the given receiver and
     * sender.
     * 
     * @param sender - the string representation of the sender QName
     * @param receiver - the string representation of the receiver QName
     * @param xml - the message
     * @return the transformed message
     */
    String transformMethodCall(String sender, String receiver, String xml);

    /**
     * Transform the xml of a return value message for the given receiver and
     * sender.
     * 
     * @param sender - the string representation of the sender QName
     * @param receiver - the string representation of the receiver QName
     * @param xml - the message
     * @return the transformed message
     */
    String transformReturnValue(String sender, String receiver, String xml);

    /**
     * Transform the xml of a event message for the given receiver and sender.
     * 
     * @param sender - the string representation of the sender QName
     * @param receiver - the string representation of the receiver QName
     * @param xml - the message
     * @return the transformed message
     */
    String transformEvent(String sender, String receiver, String xml);

}
