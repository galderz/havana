package j.mime;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

public class MimeTypes {

   public static void main(String[] args) throws MimeTypeParseException {
      // For String type
      MimeType strType = new MimeType("text/plain");
      System.out.println(strType.getPrimaryType());
      System.out.println(strType.getSubType());

      // For String type w/ charset
      MimeType strUtf8Type = new MimeType("text/plain;charset=UTF-8");
      System.out.println(strUtf8Type.getPrimaryType());
      System.out.println(strUtf8Type.getSubType());
      System.out.println(strUtf8Type.getParameters().get("charset"));

      // For POJO type
      MimeType personType = new MimeType("application/x-java-object;type=j.mime.Person");
      System.out.println(personType.getPrimaryType());
      System.out.println(personType.getSubType());
      System.out.println(personType.getParameters());
      System.out.println(personType.getParameters().get("type"));

      // For byte[] (java.io serialized)
      MimeType serializedType = new MimeType("application/x-java-serialized-object");
      System.out.println(serializedType);

      // For normal byte[], e.g. String UTF-8 representation
      MimeType bytesType = new MimeType("application/octet-stream");
      System.out.println(bytesType);
   }

}
