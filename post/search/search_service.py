from search.repository import ElasticsearchRepository


class SearchService:
    def __init__(self):
        self.repository = ElasticsearchRepository()

    def search(self, search_dto):
        search_dto = self.repository(search_dto)
        return search_dto
