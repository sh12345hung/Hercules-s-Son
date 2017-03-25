package com.thaonguyen.MongoDBConnector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

public class NewsList {
	private ArrayList<News> _list;

	public NewsList() {
		super();
		
		_list = new ArrayList<News>();
	}
	
	public NewsList(NewsList Obj) {
		super();
		
		this._list = Obj._list;
	}
	
	public ArrayList<News> toArrayList() {
		return _list;
	}
	
	public boolean addNews(News News) {
		String url = News.getURL();
		boolean addSuccessFlag = true;
		
		/* Check if News is exist in _list */
		Iterator<News> iterator = _list.iterator();
		while (iterator.hasNext()) {
			if (url.equals(iterator.next().getURL())) {
				addSuccessFlag = false;
				break;
			}
		}
		
		/* Add to _list */
		if (addSuccessFlag) {
			_list.add(News);
		}
		
		/* Return adding result */
		return addSuccessFlag;
	}
	
	public boolean SetNewsID(ArrayList<String> NewsIDArrayList) {

		/* Check if size of NewsIDArrayList is equals to _list */
		if (NewsIDArrayList.size() != _list.size()) {
			return false;
		}
		
		/* Change ID */
		Iterator<String> NewsIDIterator = NewsIDArrayList.iterator();
		Iterator<News> _listIterator = _list.iterator();
		while (_listIterator.hasNext()) {
			_listIterator.next().setID(NewsIDIterator.next());
		}
		
		/* Return success result */
		return true;
	}
	
	public int count() {
		return _list.size();
	}
	
	public List<Document> toArrayListDocument() {
		List<Document> documents = new ArrayList<Document>();
		
		Iterator<News> iterator = _list.iterator();
		while (iterator.hasNext()) {
			documents.add(iterator.next().toDocument());
		}
		
		return documents;
	}
}
