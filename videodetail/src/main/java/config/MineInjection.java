package config;


import config.datasource.MineHttpDataSource;
import config.datasource.MineLocalDataSource;
import config.http.MineHttpDataSourceImpl;
import config.http.RetrofitClient;
import config.http.service.MineApiService;
import config.local.MineLocalDataSourceImpl;
import model.MineModel;

public class MineInjection {
    public static MineModel provideRepository() {
        //网络API服务
        MineApiService apiService = RetrofitClient.getInstance().create(MineApiService.class);
        //网络数据源
        MineHttpDataSource httpDataSource = MineHttpDataSourceImpl.getInstance(apiService);
        //本地数据源
        MineLocalDataSource localDataSource = MineLocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return MineModel.getInstance(httpDataSource, localDataSource);
    }
}
