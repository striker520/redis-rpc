package com.fkzm.rpcprovider.utils;

/**
 * @author dejavu
 * @create 2020/04/20
 */

public class CacheUtils {
    private static volatile String REDUCE_STOCK;

    public static String  getReduceStockScriptInstance(){
        if(REDUCE_STOCK==null){
            synchronized (CacheUtils.class){
                if(REDUCE_STOCK==null){
                    StringBuilder sb = new StringBuilder();
                    sb.append("local stock = redis.call('get',KEYS[1])\n")
                            .append("local inta = tonumber(stock)\n")
                            .append("local intb = tonumber(ARGV[1])\n")
                            .append("if inta >= intb then\n")
                            .append("  redis.call('set',KEYS[1],inta-intb)\n")
                            .append("  return '1'\n")
                            .append("end\n")
                            .append("return '0'\n");
                    REDUCE_STOCK=sb.toString();
                }
            }
        }
        return REDUCE_STOCK;
    }

}
