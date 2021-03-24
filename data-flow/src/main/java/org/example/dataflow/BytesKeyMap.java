package org.example.dataflow;

import java.util.Arrays;

public class BytesKeyMap
{
    final BytesKey[] keys = new BytesKey[32];
    final Object[] values = new Object[32];
    int size = 0;

    static void put(byte[] key, Object value, BytesKeyMap map)
    {
        BytesKey bytesKey = new BytesKey(key);
        int nextIndex = -1;

        for (int i = 0; i < map.size; i++)
        {
            if (map.keys[i] instanceof BytesKey thisKey)
            {
                if (Arrays.equals(thisKey.key, key))
                {
                    nextIndex = i;
                    break;
                }
            }
        }

        if (nextIndex == -1)
        {
            map.keys[map.size] = bytesKey;
            map.values[map.size] = value;
            map.size = map.size + 1;
        }
        else
        {
            map.values[nextIndex] = value;
        }
    }

    static final record BytesKey(byte[] key) {}
}
