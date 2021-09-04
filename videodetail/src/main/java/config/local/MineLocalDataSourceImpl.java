package config.local;


import config.datasource.MineLocalDataSource;

/**
 * 本地数据源，可配合Room框架使用
 * Created by tanggongwen on 2019/9/17.
 */
public class MineLocalDataSourceImpl implements MineLocalDataSource {
    private volatile static MineLocalDataSourceImpl INSTANCE = null;

    public static MineLocalDataSourceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (MineLocalDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MineLocalDataSourceImpl();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private MineLocalDataSourceImpl() {
        //数据库Helper构建
    }


}
