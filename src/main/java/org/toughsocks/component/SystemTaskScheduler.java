package org.toughsocks.component;


import com.google.gson.Gson;
import io.netty.handler.traffic.TrafficCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.toughsocks.config.SocksConfig;

import java.util.concurrent.TimeUnit;

/**
 * 定时任务设计
 */
@Component
public class SystemTaskScheduler  {


    @Autowired
    private Gson gson;

    @Autowired
    private ThreadPoolTaskExecutor systaskExecutor;

    @Autowired
    private SocksConfig socksConfig;

    @Autowired
    private Memarylogger logger;

    @Autowired
    private TrafficStat trafficStat;

    @Autowired
    private SocksStat socks5Stat;

    @Autowired
    private TicketCache ticketCache;

    @Autowired
    private SessionCache sessionCache;
    /**
     * 消息统计任务
     */
    @Scheduled(fixedDelay = 5000, initialDelay = 5000)
    public void updateTrafficStat(){
        systaskExecutor.execute(()->{
            TrafficCounter trafficCounter = socksConfig.getTrafficHandler().trafficCounter();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ignore) {

            }
            final long totalRead = trafficCounter.cumulativeReadBytes();
            final long totalWrite = trafficCounter.cumulativeWrittenBytes();
            trafficStat.updateRead(totalRead);
            trafficStat.updateWrite(totalWrite);
//            logger.print("total read: " + (totalRead >> 10) + " KB");
//            logger.print("total write: " + (totalWrite >> 10) + " KB");
//            logger.print("流量监控: " + System.lineSeparator() + trafficCounter);
        });
    }

    /**
     * 消息统计任务
     */
    @Scheduled(fixedDelay = 5000, initialDelay = 5000)
    public void updateSocksStat(){
        socks5Stat.runStat();
    }

    /**
     * 同步上网日志
     */
    @Scheduled(fixedRate = 10 * 1000)
    public void syncTicket() {
        systaskExecutor.execute(()->ticketCache.syncData());
    }

    /**
     * 同步上网日志
     */
    @Scheduled(fixedRate = 30 * 1000)
    public void clearExpire() {
        systaskExecutor.execute(()->sessionCache.clearExpireSession());
    }



}
