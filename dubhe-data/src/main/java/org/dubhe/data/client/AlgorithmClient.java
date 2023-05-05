//@FeignClient(value = ApplicationNameConst.SERVER_ALGORITHM,contextId = "algorithmClient",fallback = AlgorithmClientFallback.class)
//public interface AlgorithmClient {
//    @GetMapping("/algorithms/selectById")
//    DataResponseBody<TrainAlgorithmQureyVO> selectById(@SpringQueryMap TrainAlgorithmSelectByIdDTO trainAlgorithmSelectByIdDTO);
//}