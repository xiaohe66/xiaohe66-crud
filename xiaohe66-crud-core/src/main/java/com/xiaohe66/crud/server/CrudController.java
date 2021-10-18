package com.xiaohe66.crud.server;

import com.xiaohe66.common.dto.Page;
import com.xiaohe66.common.dto.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiaohe
 * @since 2021.09.24 11:51
 */
@RestController
@RequestMapping("/service")
@ConditionalOnBean(ICrudDispatcher.class)
@RequiredArgsConstructor
@Slf4j
public class CrudController {

    private final ICrudDispatcher crudDispatcher;

    @PostMapping("/{name}")
    public R<Object> post(@PathVariable String name, @RequestBody String body) {

        return R.ok(crudDispatcher.post(name, body));
    }

    @PutMapping("/{name}")
    public R<Object> put(@PathVariable String name, @RequestBody String body) {

        return R.ok(crudDispatcher.put(name, body));
    }

    /**
     * TODO : body 放在 url 后面好吗？是不是考虑其它方式
     */
    @GetMapping("/{name}")
    public R<Page<Object>> get(@PathVariable String name,
                               @RequestParam(value = "body") String body,
                               @RequestHeader(required = false, defaultValue = "1") int pageNo,
                               @RequestHeader(required = false, defaultValue = "15") int pageSize
    ) {

        return R.ok(crudDispatcher.get(name, body, pageNo, pageSize));
    }

    @GetMapping("/{name}/{id}")
    public R<Object> get(@PathVariable String name, @PathVariable Long id) {

        return R.ok(crudDispatcher.get(name, id));
    }

    @DeleteMapping("/{name}/{id}")
    public R<Void> delete(@PathVariable String name, @PathVariable Long id) {

        boolean isSuccess = crudDispatcher.delete(name, id);

        return isSuccess ? R.ok() : R.err();
    }

}
