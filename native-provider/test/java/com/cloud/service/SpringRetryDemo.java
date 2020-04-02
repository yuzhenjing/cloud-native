package com.cloud.service;

import org.junit.Test;
import org.springframework.classify.Classifier;
import org.springframework.retry.*;
import org.springframework.retry.backoff.*;
import org.springframework.retry.policy.*;
import org.springframework.retry.support.RetryTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SpringRetryDemo {

    private static final int NOTIFY_RETRY_TIMES = 3;

    /**
     * 简介：
     *
     * Spring-retry提供的RetryOperations接口，该接口提供了若干方法来执行重试操作
     *
     * public interface RetryOperations {
     *       <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E;
     *
     *       <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback, RecoveryCallback<T> recoveryCallback) throws E;
     *
     *       <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback, RetryState retryState) throws E, ExhaustedRetryException;
     *
     *       <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback, RecoveryCallback<T> recoveryCallback, RetryState retryState) throws E;
     * }
     *
     * 调用者通过传入RetryCallback来完成调用者的重试操作。如果callback执行失败(抛出某些异常)，那么会按照调用者设定的策略进行重试。
     * 重试操作直到成功，或根据使用者设定的条件而退出。
     *
     * RetryCallback的接口定义如下：
     *
     * public interface RetryCallback<T, E extends Throwable> {
     *
     *      T doWithRetry(RetryContext context) throws E;
     * }
     *
     */


    /**
     * TimeoutRetryPolicy策略（注:不是间隔多长时间进行重试的那个时间）
     * <p>
     * 代码定义了TimeoutRetryPolicy策略，TimeoutRetryPolicy超时时间默认是1秒。
     * TimeoutRetryPolicy超时是指在execute方法内部，从open操作开始到调用
     * TimeoutRetryPolicy的canRetry方法这之间所经过的时间。
     * 这段时间未超过TimeoutRetryPolicy定义的超时时间，那么执行操作，否则抛出异常。
     * <p>
     * protected <T, E extends Throwable> T doExecute(RetryCallback<T, E> retryCallback,RecoveryCallback<T> recoveryCallback, RetryState state) throws E,ExhaustedRetryException {
     * ……略
     * RetryContext context = open(retryPolicy, state);
     * ……略
     * while (canRetry(retryPolicy, context) &&   !context.isExhaustedOnly())
     * // 调用canRetry检查是否可以重试
     * ……略
     * }
     */
    @Test
    public void timeoutRetryPolicyTest() throws Exception {

        // Spring-retry提供了RetryOperations接口的实现类RetryTemplate
        RetryTemplate template = new RetryTemplate();

        // 超时重试策略
        TimeoutRetryPolicy policy = new TimeoutRetryPolicy();
        // 超时时间 默认 1000 毫秒
        policy.setTimeout(2000);

        // 设置重试策略
        template.setRetryPolicy(policy);

        // 执行
        String result = template.execute(
                new RetryCallback<String, Exception>() {

                    public String doWithRetry(RetryContext context) throws InterruptedException {

                        System.out.println("执行Buiness logic");
                        // TODO 对这个策略模式的理解还是模糊 还为理解超时到底是哪个地方超时
                        // 等待3秒
                        TimeUnit.SECONDS.sleep(3);

                        return "SUCCESS";
                    }
                },
                // 当重试执行完闭，操作还未成为，那么可以通过RecoveryCallback完成一些失败事后处理。
                new RecoveryCallback<String>() {
                    public String recover(RetryContext context) throws Exception {

                        return "failed callback";
                    }
                }
        );

        System.out.println(result);
    }

    /**
     * SimpleRetryPolicy 策略
     * <p>
     * will execute the callback at least once, and as many as 3 times.
     * 该策略定义了对指定的异常进行若干次重试。默认情况下，对Exception异常及其子类重试3次.
     * 如果创建SimpleRetryPolicy并指定重试异常map，可以选择性重试或不进行重试.
     */
    @SuppressWarnings("unused")
    @Test
    public void simpleRetryPolicyTest() throws Exception {

        // Spring-retry提供了RetryOperations接口的实现类RetryTemplate
        RetryTemplate template = new RetryTemplate();


        // 简单重试策略 example one
        SimpleRetryPolicy policy1 = new SimpleRetryPolicy();
        // 最大重试次数  默认3次  这里设置为5次
        policy1.setMaxAttempts(5);

        // 简单重试策略 example two  1 重试次数5次  2Exception及其子类都进行异重试
        // Set the max attempts including the initial attempt before retrying
        // and retry on all exceptions (this is the default):
        SimpleRetryPolicy policy2 = new SimpleRetryPolicy(5, Collections.singletonMap(Exception.class, true));

        // 简单重试策略 example three
        Map<Class<? extends Throwable>, Boolean> retryableExceptionMaps = new HashMap<Class<? extends Throwable>, Boolean>();
        ;
        // 空指针异常进行重试  true 进行重试(当Map中的的value为false，那么执行方法，随后抛出异常不进行重试。)
        retryableExceptionMaps.put(NullPointerException.class, true);
        SimpleRetryPolicy policy3 = new SimpleRetryPolicy(5, retryableExceptionMaps);

        // 设置重试策略
        template.setRetryPolicy(policy1);
//        template.setRetryPolicy(policy2);
//        template.setRetryPolicy(policy3);

        // 执行
        String result = template.execute(
                new RetryCallback<String, Exception>() {

                    public String doWithRetry(RetryContext context) throws InterruptedException {

                        if (true) {
                            System.out.println("执行Buiness logic. NPE");
                            // 抛出异常
                            throw new NullPointerException("NullPointerException");
                        }

                        return "SUCCESS";
                    }
                },
                // 当重试执行完闭，操作还未成为，那么可以通过RecoveryCallback完成一些失败事后处理。
                new RecoveryCallback<String>() {
                    public String recover(RetryContext context) throws Exception {

                        return "failed callback";
                    }
                }
        );

        System.out.println(result);
    }

    /**
     * NeverRetryPolicy 策略
     * <p>
     * 执行一次待执行操作，若出现异常后不进行重试。
     */
    @Test
    public void neverRetryPolicyTest() throws Exception {

        // Spring-retry提供了RetryOperations接口的实现类RetryTemplate
        RetryTemplate template = new RetryTemplate();

        // 从不重试策略
        NeverRetryPolicy policy = new NeverRetryPolicy();

        // 设置重试策略
        template.setRetryPolicy(policy);

        // 执行
        String result = template.execute(
                new RetryCallback<String, Exception>() {

                    public String doWithRetry(RetryContext context) throws InterruptedException {

                        System.out.println("执行Buiness logic. NPE");
                        // 抛出异常
                        throw new NullPointerException("NullPointerException");

                    }
                },
                // 当重试执行完闭，操作还未成为，那么可以通过RecoveryCallback完成一些失败事后处理。
                new RecoveryCallback<String>() {
                    public String recover(RetryContext context) throws Exception {

                        return "failed callback";
                    }
                }
        );

        System.out.println(result);
    }


    /**
     * AlwaysRetryPolicy 策略
     * <p>
     * 异常后一直重试直到成功。
     */
    @Test
    public void alwaysRetryPolicyTest() throws Exception {

        // Spring-retry提供了RetryOperations接口的实现类RetryTemplate
        RetryTemplate template = new RetryTemplate();

        // 一直重试策略
        AlwaysRetryPolicy policy = new AlwaysRetryPolicy();

        // 设置重试策略
        template.setRetryPolicy(policy);

        // 执行
        String result = template.execute(
                new RetryCallback<String, Exception>() {

                    public String doWithRetry(RetryContext context) throws InterruptedException {

                        System.out.println("执行Buiness logic. NPE");
                        // 抛出异常
                        throw new NullPointerException("NullPointerException");

                    }
                },
                // 当重试执行完闭，操作还未成为，那么可以通过RecoveryCallback完成一些失败事后处理。
                new RecoveryCallback<String>() {
                    public String recover(RetryContext context) throws Exception {

                        return "failed callback";
                    }
                }
        );

        System.out.println(result);
    }


    /**
     * ExceptionClassifierRetryPolicy 策略
     * <p>
     * 根据产生的异常选择重试策略。
     */
    @SuppressWarnings("serial")
    @Test
    public void exceptionClassifierRetryPolicyTest() throws Exception {

        // Spring-retry提供了RetryOperations接口的实现类RetryTemplate
        RetryTemplate template = new RetryTemplate();


        /* setPolicyMap 与 setExceptionClassifier 使用一个即可。*/

        // 方式 1 setPolicyMap
        // 根据异常设置重试策略
        ExceptionClassifierRetryPolicy policy1 = new ExceptionClassifierRetryPolicy();

        Map<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<Class<? extends Throwable>, RetryPolicy>();
        // 抛出TimeoutException采用AlwaysRetryPolicy策略
        policyMap.put(TimeoutException.class, new AlwaysRetryPolicy());
        // 抛出NullPointerException采用NeverRetryPolicy策略
        policyMap.put(NullPointerException.class, new NeverRetryPolicy());
        // 异常重试策略map
        policy1.setPolicyMap(policyMap);

        // 方式 2 setExceptionClassifier
        // 根据异常设置重试策略
        ExceptionClassifierRetryPolicy policy2 = new ExceptionClassifierRetryPolicy();
        // 此外可以通过setExceptionClassifier来为异常指定重试策略。
        Classifier<Throwable, RetryPolicy> exceptionClassifier = new Classifier<Throwable, RetryPolicy>() {
            public RetryPolicy classify(Throwable classifiable) {
                if (classifiable instanceof TimeoutException)
                    return new SimpleRetryPolicy();
                return new NeverRetryPolicy();
            }

        };
        policy2.setExceptionClassifier(exceptionClassifier);

        // 设置重试策略
        template.setRetryPolicy(policy1);
//        template.setRetryPolicy(policy2);

        // 执行
        String result = template.execute(
                new RetryCallback<String, Exception>() {

                    public String doWithRetry(RetryContext context) throws Exception {

                        int number = new Random().nextInt(10) + 1;
                        if (number > 5) {

                            System.out.println("执行Buiness logic. NPE");
                            // 抛出异常
                            throw new NullPointerException("NullPointerException");
                        } else {

                            System.out.println("执行Buiness logic. TE");
                            // 抛出异常
                            throw new TimeoutException("TimeoutException");
                        }
                    }
                },
                // 当重试执行完闭，操作还未成为，那么可以通过RecoveryCallback完成一些失败事后处理。
                new RecoveryCallback<String>() {
                    public String recover(RetryContext context) throws Exception {

                        return "failed callback";
                    }
                }
        );

        System.out.println(result);
    }

    /**
     * CompositeRetryPolicy 策略
     * <p>
     * 用户指定一组策略，随后根据optimistic选项来确认如何重试。
     * <p>
     * 下面的代码中创建CompositeRetryPolicy策略，并创建了RetryPolicy数组，
     * 数组有两个具体策略SimpleRetryPolicy与AlwaysRetryPolicy。
     * <p>
     * optimistic = true
     * 当CompositeRetryPolicy设置optimistic为true时，Spring-retry会顺序遍历RetryPolicy[]数组，如果有一个重试策略可重试，
     * 例如SimpleRetryPolicy没有达到重试次数，那么就会进行重试
     * <p>
     * optimistic = false
     * 如果optimistic选项设置为false。那么有一个重试策略无法重试，那么就不进行重试。
     * 例如SimpleRetryPolicy达到重试次数不能再重试，而AlwaysRetryPolicy可以重试，那么最终是无法重试的。
     * <p>
     * 以下代码，设置setOptimistic(true)，而AlwaysRetryPolicy一直可重试，那么最终可以不断进行重试。
     */
    @Test
    public void compositeRetryPolicyTest() throws Exception {

        // Spring-retry提供了RetryOperations接口的实现类RetryTemplate
        RetryTemplate template = new RetryTemplate();

        // 混合策略
        CompositeRetryPolicy policy = new CompositeRetryPolicy();
        // 策略组
        RetryPolicy[] polices = {new SimpleRetryPolicy(), new AlwaysRetryPolicy()};
        policy.setPolicies(polices);
        policy.setOptimistic(true);

        // 设置重试策略
        template.setRetryPolicy(policy);

        try {


            // 执行
            String result = template.execute(
                    new RetryCallback<String, Exception>() {

                        public String doWithRetry(RetryContext context) throws Exception {

                            int number = new Random().nextInt(10) + 1;
                            if (number > 5) {

                                System.out.println("执行Buiness logic. NPE");
                                // 抛出异常
                                throw new NullPointerException("NullPointerException");
                            } else {

                                System.out.println("执行Buiness logic. TE");
                                // 抛出异常
                                throw new TimeoutException("TimeoutException");
                            }
                        }
                    },
                    // 当重试执行完闭，操作还未成为，那么可以通过RecoveryCallback完成一些失败事后处理。
                    new RecoveryCallback<String>() {
                        public String recover(RetryContext context) throws Exception {

                            return "failed callback";
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退避(BackOff)策略
     *
     * 当操作执行失败时，根据设置的重试策略进行重试。通过BackoffPolicy可以设定再次重试的时间间隔。
     *
     * 接口：
     * public interface BackOffPolicy {
     *       BackOffContext start(RetryContext context);
     *       void backOff(BackOffContext backOffContext) throws BackOffInterruptedException;
     * }
     *
     * BackOff策略接口的具体实现:
     *
     * interface BackOffPolicy
     *      // 实现BackOff接口的抽象类
     *      abstract StatelessBackOffPolicy
     *          class FixedBackOffPolicy // 在等待一段固定的时间后，再进行重试。默认为1秒。
     *          class NoBackOffPolicy    // 实现了空方法，因此采用次策略，重试不会等待。这也是RetryTemplate采用的默认退避(backOff)策略
     *          class UniformRandomBackOffPolicy // 均匀随机退避策略，等待时间为 最小退避时间 + [0,最大退避时间 - 最小退避时间)间的一个随机数，如果最大退避时间等于最小退避时间那么等待时间为0。
     *      // 继承BackOff接口的接口
     *      interface SleepingBackOffPolicy
     *          class ExponentialBackOffPolicy // 指数退避策略 ，每次等待时间为 等待时间 = 等待时间 * N ，即每次等待时间为上一次的N倍。如果等待时间超过最大等待时间，那么以后的等待时间为最大等待时间。
     *              // 该类是ExponentialBackOffPolicy的子类
     *              class ExponentialRandomBackOffPolicy // 指数随机策略
     *          class FixedBackOffPolicy    // 与StatelessBackoffPolicy的同名实现类返回等待时间的方法是一致的。而两者的主要区别是，SleepingbackOffPolicy可以设置用户定义的Sleeper。
     *          class UniformRandomBackOffPolicy // 与StatelessBackoffPolicy的同名实现类返回等待时间的方法是一致的。而两者的主要区别是，SleepingbackOffPolicy可以设置用户定义的Sleeper。
     *
     */

    /**
     * 实现BackOff接口的抽象类
     * StatelessBackoffPolicy 抽象类下的退避策略
     */
    @SuppressWarnings("unused")
    @Test
    public void statelessBackoffPolicyTest() throws Exception {

        // Spring-retry提供了RetryOperations接口的实现类RetryTemplate
        RetryTemplate template = new RetryTemplate();

        // 简单重试策略
        SimpleRetryPolicy policy = new SimpleRetryPolicy(); // 默认重试3次
        // 设置重试策略
        template.setRetryPolicy(policy);

        // 1. 固定退避策略
        FixedBackOffPolicy backOffPolicy1 = new FixedBackOffPolicy();
        backOffPolicy1.setBackOffPeriod(2000); // Default back off period - 1000ms.

        // 2. 默认退避策略 不等待，直接重试
        NoBackOffPolicy backOffPolicy2 = new NoBackOffPolicy();

        // 3. 均匀随机退避策略 
        UniformRandomBackOffPolicy backOffPolicy3 = new UniformRandomBackOffPolicy();
        // 等待时间为 最小退避时间 + [0, 最大退避时间 - 最小退避时间)间的一个随机数 (如果最大退避时间等于最小退避时间那么等待时间为0)
        backOffPolicy3.setMinBackOffPeriod(2000); // Default min back off period - 500ms.
        backOffPolicy3.setMaxBackOffPeriod(5000); // Default max back off period - 1500ms.

        // 设置退避策略
//        template.setBackOffPolicy(backOffPolicy1);
//        template.setBackOffPolicy(backOffPolicy2);
        template.setBackOffPolicy(backOffPolicy3);

        // 执行
        String result = template.execute(
                new RetryCallback<String, Exception>() {

                    public String doWithRetry(RetryContext context) throws Exception {
                        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                        int number = new Random().nextInt(10) + 1;
                        if (number > 5) {

                            System.out.println("执行Buiness logic. NPE" + "开始执行时间:" + s.format(new Date()));
                            // 抛出异常
                            throw new NullPointerException("NullPointerException");
                        } else {

                            System.out.println("执行Buiness logic. TE" + "开始执行时间:" + s.format(new Date()));
                            // 抛出异常
                            throw new TimeoutException("TimeoutException");
                        }
                    }
                },
                // 当重试执行完闭，操作还未成为，那么可以通过RecoveryCallback完成一些失败事后处理。
                new RecoveryCallback<String>() {
                    public String recover(RetryContext context) throws Exception {

                        return "failed callback";
                    }
                }
        );

        System.out.println(result);
    }


    /**
     * 继承BackOff接口的接口
     * SleepingbackOffPolicy 接口下的退避策略
     */
    @SuppressWarnings("serial")
    @Test
    public void sleepingbackOffPolicyTest() throws Exception {

        // Spring-retry提供了RetryOperations接口的实现类RetryTemplate
        RetryTemplate template = new RetryTemplate();

        // 简单重试策略
        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        // 重试5次
        policy.setMaxAttempts(5);
        // 设置重试策略
        template.setRetryPolicy(policy);


        // 1. 指数退避策略
        ExponentialBackOffPolicy backOffPolicy1 = new ExponentialBackOffPolicy();
        // 每次等待时间为 等待时间 = 等待时间 * N ，即每次等待时间为上一次的N倍。 (如果等待时间超过最大等待时间，那么以后的等待时间为最大等待时间。)
        // 以下设置 初始时间间隔为2000毫秒，N = 3，¸最大间隔为6000毫秒，那么从第3次重试开始，以后每次等待时间都为6000毫秒。
        backOffPolicy1.setInitialInterval(3 * 1000);// 等待时间 The default 'initialInterval' value - 100 millisecs.
        backOffPolicy1.setMultiplier(3);// 等待倍数 The default 'multiplier' value - value 2 (100% increase per backoff).
        backOffPolicy1.setMaxInterval(30 * 1000);//最大等待时间  The default maximum backoff time (30 seconds).

//        // 2. 指数随机退避策略
//        ExponentialRandomBackOffPolicy backOffPolicy2 = new ExponentialRandomBackOffPolicy();
//        // 算法是 等待时间 = 等待时间 * (1 + Random（随机数） * (N - 1))
//        backOffPolicy2.setInitialInterval(2000);// 等待时间 The default 'initialInterval' value - 100 millisecs.
//        backOffPolicy2.setMultiplier(3);// 等待倍数 The default 'multiplier' value - value 2 (100% increase per backoff).
//
//        // 3. 固定退避策略
//        FixedBackOffPolicy backOffPolicy3 = new FixedBackOffPolicy();
//        backOffPolicy3.setBackOffPeriod(2000); // Default back off period - 1000ms.
//        // 用户自定义 sleeper 不知道能干啥 不懂
//        Sleeper sleeper3 = new Sleeper() {
//
//            @Override
//            public void sleep(long backOffPeriod) throws InterruptedException {
//                // 不理解这个的作用
//                TimeUnit.SECONDS.sleep(10);
//                System.out.println("sleeper");
//            }
//        };
//        backOffPolicy3.setSleeper(sleeper3);
//
//        // 4. 均匀随机退避策略
//        UniformRandomBackOffPolicy backOffPolicy4 = new UniformRandomBackOffPolicy();
//        // 等待时间为 最小退避时间 + [0, 最大退避时间 - 最小退避时间)间的一个随机数 (如果最大退避时间等于最小退避时间那么等待时间为0)
//        backOffPolicy4.setMinBackOffPeriod(2000); // Default min back off period - 500ms.
//        backOffPolicy4.setMaxBackOffPeriod(5000); // Default max back off period - 1500ms.
//        // 用户自定义 sleeper 不知道能干啥 不懂
//        Sleeper sleeper4 = new Sleeper() {
//
//            @Override
//            public void sleep(long backOffPeriod) throws InterruptedException {
//                // 不理解这个的作用
//                TimeUnit.SECONDS.sleep(10);
//                System.out.println("sleeper");
//            }
//        };
//        backOffPolicy4.setSleeper(sleeper4);


        // 设置退避策略
        template.setBackOffPolicy(backOffPolicy1);
//        template.setBackOffPolicy(backOffPolicy2);
//        template.setBackOffPolicy(backOffPolicy3);
//        template.setBackOffPolicy(backOffPolicy4);

        // 执行
        String result = template.execute(
                new RetryCallback<String, Exception>() {

                    public String doWithRetry(RetryContext context) throws Exception {
                        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        int number = new Random().nextInt(10) + 1;
                        if (number > 5) {

                            System.out.println("执行Buiness logic. NPE" + "开始执行时间:" + s.format(new Date()));
                            // 抛出异常
                            throw new NullPointerException("NullPointerException");
                        } else {

                            System.out.println("执行Buiness logic. TE" + "开始执行时间:" + s.format(new Date()));
                            // 抛出异常
                            throw new TimeoutException("TimeoutException");
                        }
                    }
                },
                // 当重试执行完闭，操作还未成为，那么可以通过RecoveryCallback完成一些失败事后处理。
                new RecoveryCallback<String>() {
                    public String recover(RetryContext context) throws Exception {

                        return "failed callback";
                    }
                }
        );

        System.out.println(result);
    }

    /**
     * 监听器
     */
    @Test
    public void retryListenerTest() throws Exception {

        // Spring-retry提供了RetryOperations接口的实现类RetryTemplate
        RetryTemplate template = new RetryTemplate();

        // 简单重试策略
        SimpleRetryPolicy policy = new SimpleRetryPolicy(); // 默认重试3次
        // 设置重试策略
        template.setRetryPolicy(policy);

        // 固定时间退避策略
        FixedBackOffPolicy backOffPolicy1 = new FixedBackOffPolicy();
        backOffPolicy1.setBackOffPeriod(2000); // Default back off period - 1000ms.
        // 设置退避策略
        template.setBackOffPolicy(backOffPolicy1);

        /**
         *
         * 上述代码注册了两个Listener，Listener中的三个实现方法，onError, open, close会在执行重试操作时被调用，
         * 在RetryTemplate中doOpenInterceptors, doCloseInterceptors, doOnErrorInterceptors
         * 会调用监听器对应的open, close, onError 方法。
         *
         * doOpenInterceptors方法在第一次重试之前会被调用，如果该方法返回true，则会继续向下直接，如果返回false，则抛出异常，停止重试。
         *
         * doOnErrorInterceptors 在抛出异常后执行，
         *
         * doCloseInterceptors 会在重试操作执行完毕后调用。
         *
         */

        // 监听器1
        RetryListener retryListener1 = new RetryListener() {

            @Override
            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                System.out.println("1-open");
                return true;
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
                                                         Throwable throwable) {
                System.out.println("1-onError");
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
                                                       Throwable throwable) {
                System.out.println("1-close");
            }
        };

        // 监听器1
        RetryListener retryListener2 = new RetryListener() {

            @Override
            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                System.out.println("2-open");
                return true;
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
                                                         Throwable throwable) {
                System.out.println("2-onError");
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
                                                       Throwable throwable) {
                System.out.println("2-close");
            }
        };
        // 监听组
        RetryListener[] listeners = {retryListener1, retryListener2};
        /*
         *  设置监听器  当注册多个Listener时，open方法按会按Listener的注册顺序调用，
         *  而onError和close则按Listener注册的顺序逆序调用。
         *
         *  open    初始重试调用
         *  onError 发生异常就调用
         *  close   重试次数全部结束调用
         */
        template.setListeners(listeners);

        // 执行
        String result = template.execute(
                new RetryCallback<String, Exception>() {

                    public String doWithRetry(RetryContext context) throws Exception {
                        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                        int number = new Random().nextInt(10) + 1;
                        if (number > 5) {

                            System.out.println("执行Buiness logic. NPE" + "开始执行时间:" + s.format(new Date()));
                            // 抛出异常
                            throw new NullPointerException("NullPointerException");
                        } else {

                            System.out.println("执行Buiness logic. TE" + "开始执行时间:" + s.format(new Date()));
                            // 抛出异常
                            throw new TimeoutException("TimeoutException");
                        }
                    }
                },
                // 当重试执行完闭，操作还未成为，那么可以通过RecoveryCallback完成一些失败事后处理。
                new RecoveryCallback<String>() {
                    public String recover(RetryContext context) throws Exception {

                        // TODO 推送次数，推送完毕后，还没有成功，发送预警短信，通知人工处理

                        return "FAIL";
                    }
                }
        );

        System.out.println(result);
    }


    @Test
    public void realRetryListenerTest() throws Exception {

        // Spring-retry提供了RetryOperations接口的实现类RetryTemplate
        RetryTemplate template = new RetryTemplate();

        // 简单重试策略
        SimpleRetryPolicy policy = new SimpleRetryPolicy(); // 默认重试3次
        // 设置重试策略
        template.setRetryPolicy(policy);

        // 固定时间退避策略
        FixedBackOffPolicy backOffPolicy1 = new FixedBackOffPolicy();
        backOffPolicy1.setBackOffPeriod(2000); // Default back off period - 1000ms.
        // 设置退避策略
        template.setBackOffPolicy(backOffPolicy1);
        // 监听器1
        RetryListener retryListener1 = new RetryListener() {

            @Override
            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                System.out.println("1-open");
                return true;
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
                                                         Throwable throwable) {
                System.out.println("1-onError");
                System.out.println("save FAIL notify info");
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
                                                       Throwable throwable) {
                if (context.getRetryCount() < NOTIFY_RETRY_TIMES) {
                    // TODO 表示没有到达重试次数 就成功了。在这儿入库，保存通知成功信息。
                    System.out.println("save SUCCESS notify info");
                }
                System.out.println("1-close" + "重试了" + context.getRetryCount() + "次");
            }
        };
        // 监听组
        RetryListener[] listeners = {retryListener1};
        template.setListeners(listeners);

        // 执行
        String result = template.execute(
                new RetryCallback<String, Exception>() {

                    public String doWithRetry(RetryContext context) throws Exception {
                        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                        int number = new Random().nextInt(10) + 1;
                        if (number > 5) {

                            System.out.println("执行Buiness logic. NPE" + "开始执行时间:" + s.format(new Date()));
                            // 抛出异常
                            throw new NullPointerException("NullPointerException");
                        }

                        return "SUCCESS";
                    }
                },
                // 当重试执行完闭，操作还未成为，那么可以通过RecoveryCallback完成一些失败事后处理。
                new RecoveryCallback<String>() {
                    public String recover(RetryContext context) throws Exception {


                        return "FAIL";
                    }
                }
        );

        System.out.println(result);
    }

}