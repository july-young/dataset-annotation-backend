# 数据标注平台（后端）
# dataset-annotation-backend 

第三方组件依赖：mysql、redis、elasticsearch、nacos、minio

导入sql文件 <br>
导入nacos配置文件 <br>
修改地址等参数 <br>

依次启动模块 <br>
gateway <br>
auth <br>
admin <br>
dubhe-data <br>
dubhe-data-task <br>


### 转移数据集拥有人
```sql
UPDATE `data_dataset` SET origin_user_id= 3 WHERE id=37;
UPDATE `data_dataset_version` SET origin_user_id= 3 WHERE id=37;
UPDATE `data_file_1` SET origin_user_id= 3 WHERE dataset_id = 37;
```

