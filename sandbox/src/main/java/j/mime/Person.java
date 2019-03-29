package j.mime;

import java.io.Serializable;

class Person implements Serializable {

   final String firstName;
   final String lastName;

   Person(String firstName, String lastName) {
      this.firstName = firstName;
      this.lastName = lastName;
   }

   @Override
   public String toString() {
      return "Person{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
   }

}
