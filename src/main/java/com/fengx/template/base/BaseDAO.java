package com.fengx.template.base;

import com.fengx.template.pojo.page.PageParam;
import com.fengx.template.utils.jpa.DynamicSpecUtils;
import com.fengx.template.utils.jpa.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 接口静态方法和默认方法类似，只是接口静态方法不可以被接口实现类重写。
 * 告诉JPA不要创建对应接口的bean对象
 */
@NoRepositoryBean
public interface BaseDAO<T> extends JpaRepository<T, String>, JpaSpecificationExecutor<T> {

    /**
     * 默认分页
     *
     * @param pager 分页参数
     * @return page
     */
    default Page<T> findAll(PageParam pager) {
        return findAll(DynamicSpecUtils.bySearchFilter(pager), PageUtils.buildPageRequest(pager));
    }
}