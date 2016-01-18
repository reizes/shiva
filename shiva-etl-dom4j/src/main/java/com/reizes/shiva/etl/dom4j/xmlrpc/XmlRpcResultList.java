package com.reizes.shiva.etl.dom4j.xmlrpc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.reizes.shiva.utils.BeanUtil;

/**
 *  * XML-RPC 리스트형 결과를 표현하기 위한 모델
 * @author reizes
 * @param <T> -
 * @since 2010.2.1
 */
public class XmlRpcResultList<T extends XmlRpcResult> implements List<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2477159991500226396L;
	protected List<T> resultList;
	private Class<T> cls;

	public XmlRpcResultList(Class<T> cls) {
		this.cls = cls;
	}

	@Override
	public boolean add(T elm) {
		return resultList.add(elm);
	}

	@Override
	public void add(int index, T element) {
		resultList.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends T> col) {
		return resultList.addAll(col);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> col) {
		return resultList.addAll(index, col);
	}

	@Override
	public void clear() {
		resultList.clear();
	}

	@Override
	public boolean contains(Object obj) {
		return resultList.contains(obj);
	}

	@Override
	public boolean containsAll(Collection<?> col) {
		return resultList.containsAll(col);
	}

	@Override
	public T get(int index) {
		return resultList.get(index);
	}

	@Override
	public int indexOf(Object obj) {
		return resultList.indexOf(obj);
	}

	@Override
	public boolean isEmpty() {
		return resultList.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return resultList.iterator();
	}

	@Override
	public int lastIndexOf(Object obj) {
		return resultList.lastIndexOf(obj);
	}

	@Override
	public ListIterator<T> listIterator() {
		return resultList.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return resultList.listIterator(index);
	}

	@Override
	public boolean remove(Object obj) {
		return resultList.remove(obj);
	}

	@Override
	public T remove(int index) {
		return resultList.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> col) {
		return resultList.removeAll(col);
	}

	@Override
	public boolean retainAll(Collection<?> col) {
		return resultList.retainAll(col);
	}

	@Override
	public T set(int index, T element) {
		return resultList.set(index, element);
	}

	@Override
	public int size() {
		return resultList == null ? 0 : resultList.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return resultList.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return resultList.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] arr) {
		return resultList.toArray(arr);
	}

	/**
	 * Klocwork 경고를 없애기 위한 override
	 * @param result
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @see com.naver.blackbean.model.CommonXmlRpcResult#valueOf(java.util.Map)
	 */
	public void valueOf(Map<String, String> result) throws IllegalAccessException,
		InvocationTargetException,
		NoSuchMethodException {
		BeanUtil.setFromMap(this, result);
	}

	public void valueOf(Map<String, String>[] result) throws Exception {
		if (result != null) {
			resultList = new ArrayList<T>();

			for (int i = 0; i < result.length; i++) {
				Map<String, ?> map = result[i];
				T item = cls.newInstance();
				item.valueOf(map);
				resultList.add(item);
			}
		}
	}

	public void valueOf(List<Map<String, String>> result) throws Exception {
		if (result != null) {
			resultList = new ArrayList<T>();

			for (Map<String, ?> map : result) {
				T item = cls.newInstance();
				item.valueOf(map);
				resultList.add(item);
			}
		}
	}

}
