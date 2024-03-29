package com.andon.springbootutil.service;

import com.andon.springbootutil.controller.H2TestController;
import com.andon.springbootutil.response.CommonResponse;
import com.andon.springbootutil.mapstruct.H2TableMapper;
import com.andon.springbootutil.entity.H2Table;
import com.andon.springbootutil.repository.H2TableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * @author Andon
 * 2022/10/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TestH2Service {

    private final H2TableMapper h2TableMapper;
    private final H2TableRepository h2TableRepository;

    public H2Table add(H2TestController.H2TableAddVO h2TableAddVO) {
        H2Table h2Table = h2TableMapper.h2TableAddVOToH2Table(h2TableAddVO);
        return h2TableRepository.saveAndFlush(h2Table);
    }

    public void delete(String id) {
        h2TableRepository.deleteById(id);
    }

    public H2Table update(H2TestController.H2TableUpdateVO h2TableUpdateVO) {
        H2Table h2Table = h2TableMapper.h2TableUpdateVOToH2Table(h2TableUpdateVO);
        return h2TableRepository.saveAndFlush(h2Table);
    }

    public H2Table query(String id) {
        Optional<H2Table> h2TableOptional = h2TableRepository.findById(id);
        Assert.isTrue(h2TableOptional.isPresent(), "ID不存在");
        return h2TableOptional.get();
    }

    public CommonResponse<List<H2Table>> queryAll() {
        List<H2Table> h2Tables = h2TableRepository.findAll();
        CommonResponse<List<H2Table>> response = CommonResponse.successResponse(h2Tables);
        response.setTotal(h2Tables.size());
        return response;
    }
}
