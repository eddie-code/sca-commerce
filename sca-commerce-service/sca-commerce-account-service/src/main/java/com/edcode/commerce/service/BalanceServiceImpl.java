package com.edcode.commerce.service;

import com.edcode.commerce.account.BalanceInfo;
import com.edcode.commerce.dao.ScaCommerceBalanceDao;
import com.edcode.commerce.entity.ScaCommerceBalance;
import com.edcode.commerce.filter.AccessContext;
import com.edcode.commerce.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 用于余额相关服务接口实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BalanceServiceImpl implements IBalanceService {

    private final ScaCommerceBalanceDao balanceDao;

    public BalanceServiceImpl(ScaCommerceBalanceDao balanceDao) {
        this.balanceDao = balanceDao;
    }

    @Override
    public BalanceInfo getCurrentUserBalanceInfo() {

        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        BalanceInfo balanceInfo = new BalanceInfo(
                loginUserInfo.getId(), 0L
        );

        ScaCommerceBalance scaCommerceBalance = balanceDao.findByUserId(loginUserInfo.getId());
        if (null != scaCommerceBalance) {
            balanceInfo.setBalance(scaCommerceBalance.getBalance());
        } else {
            // 如果还没有用户余额记录, 这里创建出来，余额设定为0即可 (懒加载的思想)
            ScaCommerceBalance newBalance = new ScaCommerceBalance();
            newBalance.setUserId(loginUserInfo.getId());
            newBalance.setBalance(0L);
            log.info("初始化用户余额记录: [{}]", balanceDao.save(newBalance).getId());
        }

        return balanceInfo;
    }

    @Override
    public BalanceInfo deductBalance(BalanceInfo balanceInfo) {

        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        // 扣减用户余额的一个基本原则: 扣减额 <= 当前用户余额
        ScaCommerceBalance scaCommerceBalance = balanceDao.findByUserId(loginUserInfo.getId());

        // 当前余额 减去 传入的用户余额 小于 0 就抛出异常
        if (null == scaCommerceBalance || scaCommerceBalance.getBalance() - balanceInfo.getBalance() < 0) {
            throw new RuntimeException("用户余额不足!");
        }

        // 重新设置扣减之后的余额
        Long sourceBalance = scaCommerceBalance.getBalance();
        scaCommerceBalance.setBalance(scaCommerceBalance.getBalance() - balanceInfo.getBalance());
        // 重新设置后保存到数据库
        ScaCommerceBalance scaCommerceBalance1 = balanceDao.save(scaCommerceBalance);
        log.info("扣除余额: [{}], [{}], [{}]",
                // 获取保存之后的主键id
                scaCommerceBalance1.getId(),
                // 源余额
                sourceBalance,
                balanceInfo.getBalance());

        return new BalanceInfo(
                scaCommerceBalance.getUserId(),
                scaCommerceBalance.getBalance()
        );
    }
}
