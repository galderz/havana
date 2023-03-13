package util.bitmap.v1;

import java.util.Objects;

final class UnsignedWord
{
    final String fieldName;

    UnsignedWord(String fieldName)
    {
        this.fieldName = fieldName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnsignedWord that = (UnsignedWord) o;
        return Objects.equals(fieldName, that.fieldName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(fieldName);
    }
}
