package p.regex;

import java.util.regex.Pattern;

public class PatternMatching {

   public static void main(String[] args) {
//      String pattern =
//         "cipherSuite=(?:TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384|TLS_AES_128_GCM_SHA256)";

//      String pattern =
//         "cipherSuite=(?:TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384|TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256|TLS_AES_128_GCM_SHA256)";

//      String pattern =
//         "cipherSuite=(?:TLS_ECDHE_ECDSA_WITH_AES_(?:256_GCM_SHA384|128_GCM_SHA256)|TLS_AES_128_GCM_SHA256)";

//      String pattern =
//         "cipherSuite=(?:TLS_(?:ECDHE_ECDSA_WITH_|)AES_(?:256_GCM_SHA384|128_GCM_SHA256))";

      String pattern =
         "cipherSuite=TLS_.* ";

      String v1 = "cipherSuite=TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 ";
      assert Pattern.matches(pattern, v1);

      String v2 = "cipherSuite=TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 ";
      assert Pattern.matches(pattern, v2);

      String v3 = "cipherSuite=TLS_AES_128_GCM_SHA256 ";
      assert Pattern.matches(pattern, v3);
   }

}
