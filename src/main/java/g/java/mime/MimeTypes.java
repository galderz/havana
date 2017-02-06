package g.java.mime;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

public class MimeTypes {

   public static void main(String[] args) throws MimeTypeParseException {
      // For String type
      MimeType strType = new MimeType("text/plain");
      System.out.println(strType.getPrimaryType());
      System.out.println(strType.getSubType());

      // For POJO type
      MimeType personType = new MimeType("application/x-java-object;type=g.java.mime.Person");
      System.out.println(personType.getPrimaryType());
      System.out.println(personType.getSubType());
      System.out.println(personType.getParameters());
      System.out.println(personType.getParameters().get("type"));

      // For byte[] (java.io serialized)
      MimeType serializedType = new MimeType("application/x-java-serialized-object");
      System.out.println(serializedType);
   }

}
