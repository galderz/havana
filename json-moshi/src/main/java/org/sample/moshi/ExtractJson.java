package org.sample.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

public class ExtractJson
{
    public static void main(String[] args) throws IOException
    {
        Asserts.needEnabledAsserts();

        {
            final Moshi moshi = new Moshi.Builder().build();
            final JsonAdapter<BuildStats> jsonAdapter = moshi.adapter(BuildStats.class);
            final BuildStats buildStats = jsonAdapter.fromJson(JSON_WITH_PGO);
            assert buildStats != null;
            assert buildStats.general_info.graal_compiler != null;
            assert buildStats.general_info.graal_compiler.pgo != null;
            assert buildStats.general_info.graal_compiler.pgo.length == 1;
            assert buildStats.general_info.graal_compiler.pgo[0].equals("instrument");
        }

        {
            final Moshi moshi = new Moshi.Builder().build();
            final JsonAdapter<BuildStats> jsonAdapter = moshi.adapter(BuildStats.class);
            final BuildStats buildStats = jsonAdapter.fromJson(JSON_EE);
            assert buildStats != null;
            assert buildStats.general_info.graal_compiler != null;
            assert buildStats.general_info.graal_compiler.pgo != null;
            assert buildStats.general_info.graal_compiler.pgo.length == 1;
            assert buildStats.general_info.graal_compiler.pgo[0].equals("ML-inferred");
        }

        {
            final Moshi moshi = new Moshi.Builder().build();
            final JsonAdapter<BuildStats> jsonAdapter = moshi.adapter(BuildStats.class);
            final BuildStats buildStats = jsonAdapter.fromJson(JSON_CE);
            assert buildStats != null;
            assert buildStats.general_info.graal_compiler != null;
            assert buildStats.general_info.graal_compiler.pgo == null;
        }
    }

    public record BuildStats(GeneralInfo general_info) {}

    public record GeneralInfo(GraalCompiler graal_compiler) {}

    public record GraalCompiler(String[] pgo) {}

    private final static String JSON_CE =
        """
        {
            "resource_usage": {
                "memory": {
                    "system_total": 34359738368,
                    "peak_rss_bytes": 1643069440
                },
                "garbage_collection": {
                    "count": 149,
                    "total_secs": 1.907,
                    "max_heap": 25963790336
                },
                "cpu": {
                    "load": 5.481472964694502,
                    "parallelism": 10,
                    "total_cores": 10
                },
                "total_secs": 30.512299917
            },
            "image_details": {
                "code_area": {
                    "bytes": 10002880,
                    "compilation_units": 16029
                },
                "total_bytes": 23850496,
                "image_heap": {
                    "bytes": 13320192,
                    "objects": {
                        "count": 153272
                    },
                    "resources": {
                        "bytes": 158440,
                        "count": 51
                    }
                }
            },
            "general_info": {
                "c_compiler": "cc (apple, arm64, 15.0.0)",
                "name": "fibula-samples-999-SNAPSHOT-runner",
                "java_version": "21.0.2+13",
                "garbage_collector": "Serial GC",
                "graal_compiler": {
                    "march": "armv8-a",
                    "optimization_level": "2"
                },
                "vendor_version": "GraalVM CE 21.0.2+13.1",
                "graalvm_version": "GraalVM CE 21.0.2+13.1"
            },
            "analysis_results": {
                "types": {
                    "total": 6957,
                    "reflection": 2017,
                    "jni": 61,
                    "reachable": 5600
                },
                "methods": {
                    "foreign_downcalls": -1,
                    "total": 50931,
                    "reflection": 3062,
                    "jni": 55,
                    "reachable": 25993
                },
                "classes": {
                    "total": 6957,
                    "reflection": 2017,
                    "jni": 61,
                    "reachable": 5600
                },
                "fields": {
                    "total": 27510,
                    "reflection": 1033,
                    "jni": 66,
                    "reachable": 7487
                }
            }
        }
        """;

    private final static String JSON_EE =
        """
        {
            "resource_usage": {
                "memory": {
                    "system_total": 34359738368,
                    "peak_rss_bytes": 1787314176
                },
                "garbage_collection": {
                    "count": 315,
                    "total_secs": 2.8,
                    "max_heap": 25963790336
                },
                "cpu": {
                    "load": 7.253380100628931,
                    "parallelism": 10,
                    "total_cores": 10
                },
                "total_secs": 39.289728042
            },
            "image_details": {
                "code_area": {
                    "bytes": 12808976,
                    "compilation_units": 15819
                },
                "total_bytes": 25337056,
                "image_heap": {
                    "bytes": 11960320,
                    "objects": {
                        "count": 163438
                    },
                    "resources": {
                        "bytes": 158432,
                        "count": 51
                    }
                }
            },
            "general_info": {
                "c_compiler": "cc (apple, arm64, 15.0.0)",
                "name": "fibula-samples-999-SNAPSHOT-runner",
                "java_version": "21.0.5+9-LTS",
                "garbage_collector": "Serial GC",
                "graal_compiler": {
                    "pgo": [
                        "ML-inferred"
                    ],
                    "march": "armv8-a",
                    "optimization_level": "2"
                },
                "vendor_version": "Oracle GraalVM 21.0.5+9.1",
                "graalvm_version": "Oracle GraalVM 21.0.5+9.1"
            },
            "analysis_results": {
                "types": {
                    "total": 7070,
                    "reflection": 2031,
                    "jni": 61,
                    "reachable": 5650
                },
                "methods": {
                    "foreign_downcalls": -1,
                    "total": 53601,
                    "reflection": 3052,
                    "jni": 55,
                    "reachable": 28304
                },
                "classes": {
                    "total": 7070,
                    "reflection": 2031,
                    "jni": 61,
                    "reachable": 5650
                },
                "fields": {
                    "total": 27672,
                    "reflection": 1058,
                    "jni": 65,
                    "reachable": 7566
                }
            }
        }
        """;

    private final static String JSON_WITH_PGO =
        """
        {
            "resource_usage": {
                "memory": {
                    "system_total": 34359738368,
                    "peak_rss_bytes": 4262723584
                },
                "garbage_collection": {
                    "count": 191,
                    "total_secs": 5.067,
                    "max_heap": 25963790336
                },
                "cpu": {
                    "load": 6.592495414698694,
                    "parallelism": 10,
                    "total_cores": 10
                },
                "total_secs": 53.818402416999994
            },
            "image_details": {
                "code_area": {
                    "bytes": 24921712,
                    "compilation_units": 16523
                },
                "total_bytes": 85775600,
                "image_heap": {
                    "bytes": 57311232,
                    "objects": {
                        "count": 1695861
                    },
                    "resources": {
                        "bytes": 158432,
                        "count": 51
                    }
                }
            },
            "general_info": {
                "c_compiler": "cc (apple, arm64, 15.0.0)",
                "name": "fibula-samples-999-SNAPSHOT-runner",
                "java_version": "21.0.5+9-LTS",
                "garbage_collector": "Serial GC",
                "graal_compiler": {
                    "pgo": [
                        "instrument"
                    ],
                    "march": "armv8-a",
                    "optimization_level": "2"
                },
                "vendor_version": "Oracle GraalVM 21.0.5+9.1",
                "graalvm_version": "Oracle GraalVM 21.0.5+9.1"
            },
            "analysis_results": {
                "types": {
                    "total": 7299,
                    "reflection": 2087,
                    "jni": 61,
                    "reachable": 5853
                },
                "methods": {
                    "foreign_downcalls": -1,
                    "total": 54829,
                    "reflection": 3119,
                    "jni": 55,
                    "reachable": 29177
                },
                "classes": {
                    "total": 7299,
                    "reflection": 2087,
                    "jni": 61,
                    "reachable": 5853
                },
                "fields": {
                    "total": 28022,
                    "reflection": 1060,
                    "jni": 65,
                    "reachable": 7770
                }
            }
        }
        """;
}
