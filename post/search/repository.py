from abc import ABC, abstractmethod

from elasticsearch import Elasticsearch
from elasticsearch_dsl import Search

from post.utils.utils_es import es_dsl_search_paginated


class BaseRepository(ABC):
    @abstractmethod
    def create(self, id, data):
        pass

    @abstractmethod
    def get(self, id):
        pass

    @abstractmethod
    def get_many(self, ids):
        pass

    @abstractmethod
    def search(self, query):
        pass

    @abstractmethod
    def update(self, id, data):
        pass

    @abstractmethod
    def delete(self, id):
        pass


class ElasticsearchRepository(BaseRepository):
    def __init__(self, session: Elasticsearch, index_name: str = None):
        self.es = session
        self.index_name = index_name

    def is_connected(self):
        return self.es.ping()

    def create_index(self, index_name, mappings):
        return self.es.indices.create(index=index_name, body=mappings, ignore=400)

    def delete_index(self, index_name):
        return self.es.indices.delete(index=index_name, ignore=[400, 404])

    def index_exists(self, index_name):
        return self.es.indices.exists(index=index_name)

    def create_alias(self, index_name, alias_name):
        return self.es.indices.update_aliases(
            {"actions": [{"add": {"index": index_name, "alias": alias_name}}]}
        )

    def alias_exists(self, alias_name):
        return self.es.indices.exists_alias(name=alias_name)

    def create(self, id, data, index=None):
        if index is None and self.index_name is None:
            raise TypeError(
                "Must specify and index as a class property or pass into method"
            )
        return self.es.index(index=index or self.index_name, id=id, body=data)

    def get(self, id, index=None):
        if index is None and self.index_name is None:
            raise TypeError(
                "Must specify and index as a class property or pass into method"
            )
        return self.es.get(index=index or self.index_name, id=id)

    def get_many(self, ids, index=None):
        if index is None and self.index_name is None:
            raise TypeError(
                "Must specify and index as a class property or pass into method"
            )
        return self.es.mget(body={"ids": ids}, index=index or self.index_name)

    def search(self, search: Search = None, query=None, index=None):
        if index is None and self.index_name is None:
            raise TypeError(
                "Must specify and index as a class property or pass into method"
            )

        if search is not None:
            search = search.index(index or self.index_name)

            search = search.using(self.es)
            return es_dsl_search_paginated(search)

        return self.es.search(index=index or self.index_name, body=query)

    def update(self, id, data, index=None):
        if index is None and self.index_name is None:
            raise TypeError(
                "Must specify and index as a class property or pass into method"
            )
        return self.es.update(index=index or self.index_name, id=id, body={"doc": data})

    def delete(self, id, index=None):
        if index is None and self.index_name is None:
            raise TypeError(
                "Must specify and index as a class property or pass into method"
            )
        return self.es.delete(index=index or self.index_name, id=id)
