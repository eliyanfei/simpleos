package net.simpleos.impl;

import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.util.HTMLBuilder;
import net.simpleos.i.ISimpleosApplicationModule;

/**
 * @author 李岩飞
 *
 */
public abstract class AbstractCatalog extends Catalog implements IEntityBeanAware {

	private int counter;//该类目下的项目数

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getCounter() {
		return counter;
	}

	public ITreeBeanAware parent(final ISimpleosApplicationModule applicationModule) {
		return applicationModule.getBean(getClass(), getParentId());
	}

	public static class Utils {
		public static String buildCatalogTitle(final Object id, final Class<? extends AbstractCatalog> classBean,
				final ISimpleosApplicationModule applicationModule) {
			final StringBuffer buf = new StringBuffer();
			final AbstractCatalog catalog = applicationModule.getBean(classBean, id);
			buildCatalogTitle(catalog, applicationModule, buf);
			buf.append(catalog.getText());
			return buf.toString();
		}

		public static void buildCatalogTitle(final AbstractCatalog catalog, final ISimpleosApplicationModule applicationModule, final StringBuffer buf) {
			if (catalog == null)
				return;
			final AbstractCatalog pCatalog = (AbstractCatalog) catalog.parent(applicationModule);
			if (pCatalog != null) {
				buildCatalogTitle(pCatalog, applicationModule, buf);
				buf.append(pCatalog.getText()).append(HTMLBuilder.NAV);
			}
		}

		public static String buildCatalog(final Object id, final Class<? extends AbstractCatalog> classBean,
				final ISimpleosApplicationModule applicationModule, final boolean url) {
			return buildCatalog(id, classBean, applicationModule, url, null);
		}

		public static String buildCatalog(final Object id, final Class<? extends AbstractCatalog> classBean,
				final ISimpleosApplicationModule applicationModule, final boolean url, final String param) {
			final AbstractCatalog catalog = applicationModule.getBean(classBean, id);
			final StringBuffer buf = new StringBuffer();
			if (catalog == null)
				return buf.toString();
			if (catalog != null) {
				final AbstractCatalog pCatalog = (AbstractCatalog) catalog.parent(applicationModule);
				if (pCatalog != null)
					buildCatalog(pCatalog, applicationModule, buf, url, param);
				if (url) {
					buf.append(catalog.getText());
				} else
					buf.append(catalog.getText());
				buf.append(HTMLBuilder.NAV);
			}
			return buf.toString();
		}

		private static void buildCatalog(final AbstractCatalog catalog, final ISimpleosApplicationModule applicationModule, final StringBuffer buf,
				final boolean url, final String param) {
			if (url) {
				buf.append(catalog.getText());
			} else
				buf.append(catalog.getText());
			buf.append(HTMLBuilder.NAV);
		}

		/**
		 * 更新counter
		 */
		public static void updateCatalog(final Object id, final Class<? extends AbstractCatalog> classBean,
				final ISimpleosApplicationModule applicationModule, final boolean add) {
			final AbstractCatalog catalog = applicationModule.getBean(classBean, id);
			if (catalog != null)
				updateCatalog(catalog, applicationModule, add);
		}

		/**
		 * 更新counter
		 */
		public static void updateCatalog(final AbstractCatalog catalog, final ISimpleosApplicationModule applicationModule, final boolean add) {
			synchronized (applicationModule) {
				catalog.setCounter(catalog.getCounter() + (add ? 1 : -1));
				final AbstractCatalog pCatalog = (AbstractCatalog) catalog.parent(applicationModule);
				if (pCatalog == null) {
					applicationModule.doUpdate(new Object[] { "counter" }, catalog);
				} else {
					applicationModule.doUpdate(new Object[] { "counter" }, catalog, new TableEntityAdapter() {
						@Override
						public void afterUpdate(ITableEntityManager manager, Object[] objects) {
							updateParentCatalog(pCatalog, applicationModule, add);
						}
					});
				}
			}
		}

		private static void updateParentCatalog(final AbstractCatalog catalog, final ISimpleosApplicationModule applicationModule, final boolean add) {
			catalog.setCounter(catalog.getCounter() + (add ? 1 : -1));
			final AbstractCatalog pCatalog = (AbstractCatalog) catalog.parent(applicationModule);
			if (pCatalog == null) {
				applicationModule.doUpdate(new Object[] { "counter" }, catalog);
			} else {
				applicationModule.doUpdate(new Object[] { "counter" }, catalog, new TableEntityAdapter() {
					@Override
					public void afterUpdate(ITableEntityManager manager, Object[] objects) {
						updateParentCatalog(pCatalog, applicationModule, add);
					}
				});
			}
		}

		public static void statCounter(final Class<? extends AbstractCatalog> classBean, final ISimpleosApplicationModule applicationModule) {
			synchronized (applicationModule) {
				final IQueryEntitySet<? extends AbstractCatalog> qs = applicationModule.queryBean("parentId<>0", classBean);
				if (qs != null) {
					AbstractCatalog catalog = null;
					while ((catalog = qs.next()) != null) {
						final StringBuffer sql = new StringBuffer();
						sql.append("SELECT SUM(COUNTER) FROM ");
						sql.append(applicationModule.getDataObjectManager(classBean).getTablename());
						sql.append(" WHERE parentId=?");
						final long value = applicationModule.getValue(sql.toString(), new Object[] { catalog.getId() });
						if (value != 0) {
							catalog.setCounter((int) value);
							applicationModule.doUpdate(catalog);
						}
					}
				}
			}
		}

		public static String getJoinCatalog(final Object catalogId, final ISimpleosApplicationModule iItSiteApplicationModule, final Class<?> className) {
			final StringBuffer buf = new StringBuffer();
			final AbstractCatalog catalog = (AbstractCatalog) iItSiteApplicationModule.getBean(className, catalogId);
			if (catalog != null) {
				getJoinCatalog(catalog, buf, iItSiteApplicationModule, className);
			}
			buf.append(catalogId);
			return buf.toString();
		}

		public static void getJoinCatalog(final AbstractCatalog catalog, final StringBuffer buf,
				final ISimpleosApplicationModule iItSiteApplicationModule, final Class<?> className) {
			final IQueryEntitySet<?> qs = iItSiteApplicationModule.queryBean("parentId=" + catalog.getId(), className);
			AbstractCatalog docuC = null;
			while ((docuC = (AbstractCatalog) qs.next()) != null) {
				getJoinCatalog(docuC, buf, iItSiteApplicationModule, className);
				buf.append(docuC.getId()).append(",");
			}
		}
	}

}
