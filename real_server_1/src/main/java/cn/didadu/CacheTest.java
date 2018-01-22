package cn.didadu;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhangjing on 2017/12/14.
 */
public class CacheTest {

    public static void main(String[] args) throws InterruptedException {
        //guavaHeap();
        //mapdbDirect();
        mapdbDisk();
    }

    private static void guavaHeap() throws InterruptedException {
        Cache<String, String> myCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4) // 并发级别，即ConcurrentHashMap segment数量，越大并发能力越强
                .expireAfterWrite(10, TimeUnit.SECONDS) // 设置过期TTL
                .maximumSize(10000) // 设置缓存的容量，当超出时，按照LRU进行回收
                .build();
        myCache.put("guava", "heap_guava");

        while (true) {
            String value = myCache.getIfPresent("guava");
            if (value == null) {
                System.out.println("Cache expired");
                return;
            } else {
                System.out.println(value);
            }
            Thread.sleep(1000);
        }
    }

    private static void mapdbDirect() throws InterruptedException {
       HTreeMap myCache = DBMaker.memoryDirectDB()
               .concurrencyScale(16)
               .make().hashMap("myCache")
               .expireStoreSize(64 * 1024 * 1024)
               .expireMaxSize(10000)
               .expireAfterCreate(10, TimeUnit.SECONDS)
               .expireAfterUpdate(10, TimeUnit.SECONDS)
               .expireAfterGet(10, TimeUnit.SECONDS)
               .create();

        myCache.put("mapdb", "direct_mapdb");
        System.out.println(myCache.get("mapdb"));
        Thread.sleep(15000);
        System.out.println(myCache.get("mapdb"));
    }

    private static void mapdbDisk() {
        DB db = DBMaker.fileDB("/Users/zhangjing/mpdb.data")
                .fileMmapEnable() // 启用mmap
                .fileMmapEnableIfSupported() // 在支持的平台上启用mmap
                .fileMmapPreclearDisable() // 让mmap更快
                .cleanerHackEnable() // 一些BUG处理
                .transactionEnable() // 启用事务
                .closeOnJvmShutdown()
                .concurrencyScale(16).make();

        HTreeMap myCache = db.hashMap("myCache")
                .expireMaxSize(10000)
                .expireAfterCreate(10, TimeUnit.SECONDS)
                .expireAfterUpdate(10, TimeUnit.SECONDS)
                .expireAfterGet(10, TimeUnit.SECONDS)
                .createOrOpen();

        myCache.put("mapdb", "disk_mapdb");
        db.commit();
    }

}
