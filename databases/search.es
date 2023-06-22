get search_temp/_search
DELETE search_temp
put search_temp
get search_temp/_mapping
PUT search_temp/_mapping
{
      "properties" : {
        "search_term" : { "type": "text"},
        "search_tags" : {"type": "flattened"},
        "search_results" : { "type": "flattened"},
        "date_searched" : { "type": "date"},
        "search_source" : {"type": "keyword"},
        "search_dest" : {"type": "keyword"},
        "time_taken": {"type": "integer"},
        "total_result_count": {"type": "integer"}
      }
}