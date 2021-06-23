package com.kilcote.utils;

import java.util.ArrayList;
import java.util.List;

import com.kilcote.common.data.Tree;
import com.kilcote.common.data.Router;

/**
 * @author Shun fu
 */
public class TreeUtil {

	private final static Long TOP_NODE_ID = 0L;

	/**
	 * Used to build a menu or department tree
	 *
	 * @param nodes nodes
	 * @return <T> List<? extends Tree>
	 */
	public static <T> List<? extends Tree<?>> build(List<? extends Tree<T>> nodes) {
		if (nodes == null) {
			return null;
		}
		List<Tree<T>> topNodes = new ArrayList<>();
		nodes.forEach(node -> {
			Long pid = node.getParentId();
			if (pid == null || TOP_NODE_ID.equals(pid)) {
				topNodes.add(node);
				return;
			}
			for (Tree<T> n : nodes) {
				Long id = n.getId();
				if (id != null && id.equals(pid)) {
					if (n.getChild() == null) {
						n.initChildren();
					}
					n.getChild().add(node);
					node.setHasParent(true);
					n.setHasChildren(true);
					n.setHasParent(true);
					return;
				}
			}
			if (topNodes.isEmpty()) {
				topNodes.add(node);
			}
		});
		return topNodes;
	}


	/**
	 * 构造前端路由
	 *
	 * @param routes routes
	 * @param <T>    T
	 * @return ArrayList<VueRouter < T>>
	 */
	public static <T> List<Router<T>> buildVueRouter(List<Router<T>> routes) {
		if (routes == null) {
			return null;
		}
		List<Router<T>> topRoutes = new ArrayList<>();
		routes.forEach(route -> {
			Long parentId = route.getParentId();
			if (parentId == null || TOP_NODE_ID.equals(parentId)) {
				topRoutes.add(route);
				return;
			}
			for (Router<T> parent : routes) {
				Long id = parent.getId();
				if (id != null && id.equals(parentId)) {
					if (parent.getChild() == null) {
						parent.initChildren();
					}
					parent.getChild().add(route);
					parent.setAlwaysShow(true);
					parent.setHasChildren(true);
					route.setHasParent(true);
					parent.setHasParent(true);
					return;
				}
			}
		});
//		VueRouter<T> router = new VueRouter<>();
//		VueRouter<T> router404 = new VueRouter<>();
//		router404.setName("404");
//		router404.setComponent("error-page/404");
//		router404.setPath("*");
//		topRoutes.add(router404);
		return topRoutes;
	}
}