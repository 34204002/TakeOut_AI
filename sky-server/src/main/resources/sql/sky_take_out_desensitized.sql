/*
 Navicat Premium Data Transfer

 Source Server         : MyComputer
 Source Server Type    : MySQL
 Source Server Version : 80041
 Source Host           : localhost:3306
 Source Schema         : sky_take_out

 Target Server Type    : MySQL
 Target Server Version : 80041
 File Encoding         : 65001

 Date: 12/05/2026 14:11:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address_book
-- ----------------------------
DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `consignee` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '收货人',
  `sex` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '手机号',
  `province_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省级区划编号',
  `province_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省级名称',
  `city_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '市级区划编号',
  `city_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '市级名称',
  `district_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区级区划编号',
  `district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区级名称',
  `detail` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '默认 0 否 1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '地址簿' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address_book
-- ----------------------------
INSERT INTO `address_book` VALUES (2, 4, 'user001', '0', '13800000001', '36', '江西省', '3601', '市', '360112', '新建区', 'ncu', '3', 0);
INSERT INTO `address_book` VALUES (3, 4, 'user001', '0', '13800000001', '36', '江西省', '3609', '市', '360921', '奉新县', '冯川镇', '2', 1);

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` int NULL DEFAULT NULL COMMENT '类型   1 菜品分类 2 套餐分类',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '分类名称',
  `sort` int NOT NULL DEFAULT 0 COMMENT '顺序',
  `status` int NULL DEFAULT NULL COMMENT '分类状态 0:禁用，1:启用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_category_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '菜品及套餐分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (11, 1, '酒水饮料', 10, 1, '2022-06-09 22:09:18', '2025-11-09 17:15:58', 1, 0);
INSERT INTO `category` VALUES (12, 1, '传统主食', 9, 1, '2022-06-09 22:09:32', '2022-06-09 22:18:53', 1, 1);
INSERT INTO `category` VALUES (13, 2, '人气套餐', 12, 1, '2022-06-09 22:11:38', '2022-06-10 11:04:40', 1, 1);
INSERT INTO `category` VALUES (15, 2, '商务套餐', 13, 1, '2022-06-09 22:14:10', '2022-06-10 11:04:48', 1, 1);
INSERT INTO `category` VALUES (16, 1, '蜀味烤鱼', 4, 1, '2022-06-09 22:15:37', '2022-08-31 14:27:25', 1, 1);
INSERT INTO `category` VALUES (17, 1, '蜀味牛蛙', 5, 1, '2022-06-09 22:16:14', '2022-08-31 14:39:44', 1, 1);
INSERT INTO `category` VALUES (18, 1, '特色蒸菜', 6, 1, '2022-06-09 22:17:42', '2022-06-09 22:17:42', 1, 1);
INSERT INTO `category` VALUES (19, 1, '新鲜时蔬', 7, 1, '2022-06-09 22:18:12', '2022-06-09 22:18:28', 1, 1);
INSERT INTO `category` VALUES (20, 1, '水煮鱼', 8, 1, '2022-06-09 22:22:29', '2022-06-09 22:23:45', 1, 1);
INSERT INTO `category` VALUES (21, 1, '汤类', 11, 1, '2022-06-10 10:51:47', '2022-06-10 10:51:47', 1, 1);

-- ----------------------------
-- Table structure for customer_service_knowledge
-- ----------------------------
DROP TABLE IF EXISTS `customer_service_knowledge`;
CREATE TABLE `customer_service_knowledge`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题分类',
  `keywords` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关键词（逗号分隔）',
  `question` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标准问题',
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标准答案',
  `sort_weight` int NULL DEFAULT 0 COMMENT '排序权重',
  `status` int NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客服知识库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer_service_knowledge
-- ----------------------------
INSERT INTO `customer_service_knowledge` VALUES (1, '配送', '配送时间,多久,多长时间,送达', '配送需要多长时间？', '我们的标准配送时间为 30-45 分钟。具体时长会根据距离、天气和订单量有所调整。您可以在订单详情页查看预计送达时间。', 1, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (2, '配送', '配送范围,多远,哪里送', '你们的配送范围是多少？', '我们提供周边 5 公里范围内的配送服务。您可以在首页输入地址，系统会自动判断是否在配送范围内。超出范围暂不支持配送，敬请谅解。', 2, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (3, '配送', '配送费,运费,多少钱', '配送费怎么计算？', '配送费根据距离计算：\n- 3公里以内：¥3\n- 3-5公里：¥5\n- 满¥50免配送费\n\n特殊天气或高峰期可能略有调整，以实际下单为准。', 3, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (4, '配送', '超时,迟到,延误,晚了', '配送超时了怎么办？', '非常抱歉给您带来不便！如果订单超过预计时间 15 分钟仍未送达，您可以：\n1. 联系骑手查看具体情况\n2. 申请部分退款作为补偿\n3. 联系客服处理\n\n我们会持续优化配送效率，感谢您的理解！', 4, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (5, '配送', '修改地址,改地址,地址错了', '下单后可以修改配送地址吗？', '在商家接单前，您可以取消订单重新下单。\n\n商家接单后，如新地址在配送范围内且距离变化不大，可联系骑手协商。如距离较远，建议取消订单重新下单。', 5, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (6, '配送', '联系骑手,打电话,找不到', '如何联系骑手？', '您可以在订单详情页点击\"联系骑手\"按钮，直接拨打电话或使用在线聊天功能。\n\n如骑手未接听，可能是正在配送中，请稍后再试或联系客服协助。', 6, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (7, '配送', '放哪里,门卫,前台,门口', '可以让骑手把餐放在门口吗？', '可以的！您可以在订单备注中说明：\"请放门口/门卫/前台\"。\n\n但为了食品安全，建议您尽快取餐。如有丢失风险，建议选择当面签收。', 7, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (8, '订单', '取消订单,退单,不要了', '如何取消订单？', '取消订单规则：\n\n✅ 商家接单前：可直接取消，全额退款\n⚠️ 商家接单后：需联系商家协商，可能产生费用\n❌ 配送中：无法取消，建议拒收或联系客服\n\n请在\"我的订单\"中找到对应订单，点击\"取消订单\"即可。', 1, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (9, '订单', '修改订单,加菜,减菜,改备注', '下单后可以修改订单内容吗？', '商家接单前：可以取消订单重新下单。\n\n商家接单后：\n- 加菜：需重新下单\n- 减菜：联系商家协商\n- 改备注：直接联系骑手或商家\n\n建议下单前仔细确认订单内容哦~', 2, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (10, '订单', '订单状态,到哪了,进度', '如何查看订单状态？', '您可以在\"我的订单\"中实时查看订单状态：\n\n📝 待支付 → 待接单 → 制作中 → 配送中 → 已完成\n\n每个状态变更都会通过消息通知您。配送中还可查看骑手位置和预计到达时间。', 3, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (11, '订单', '订单号,在哪里看', '订单号在哪里查看？', '订单号位于：\n1. \"我的订单\"列表中，每个订单卡片上方\n2. 订单详情页顶部\n3. 支付成功页面\n4. 短信通知中\n\n订单号格式：202605120001（日期+序号），客服咨询时请提供此号码。', 4, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (12, '订单', '重复下单,下错了,多下了', '我不小心重复下单了怎么办？', '别担心！您可以：\n\n1. 立即取消其中一个订单（商家接单前可免费取消）\n2. 如已接单，联系商家说明情况，协商取消一个\n3. 两个都收下也可以哦~ 😊\n\n建议下次下单前确认购物车内容，避免重复。', 5, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (13, '订单', '历史订单,以前的订单', '如何查看历史订单？', '在\"我的订单\"页面：\n1. 默认显示最近 3 个月的订单\n2. 点击\"全部订单\"可查看历史记录\n3. 支持按时间、状态筛选\n4. 可对已完成订单进行评价\n\n订单记录永久保存，随时可查。', 6, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (14, '支付', '支付方式,微信,支付宝', '支持哪些支付方式？', '我们支持以下支付方式：\n\n💳 微信支付\n💳 支付宝\n\n暂不支持货到付款、银行卡支付等方式。支付过程安全加密，请放心使用。', 1, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (15, '支付', '退款,退钱,什么时候到账', '退款多久能到账？', '退款时效：\n\n✅ 微信支付：1-3 个工作日原路返回\n✅ 支付宝：1-3 个工作日原路返回\n\n特殊情况（如银行处理延迟）可能延长至 7 个工作日。如超时未收到，请联系客服查询。', 2, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (16, '支付', '优惠券,红包,折扣', '如何使用优惠券？', '使用优惠券步骤：\n\n1. 将商品加入购物车\n2. 结算页面自动显示可用优惠券\n3. 选择要使用的优惠券\n4. 确认优惠金额后提交订单\n\n注意：\n- 每张订单限用 1 张优惠券\n- 优惠券有有效期，请及时使用\n- 部分特价商品不可叠加优惠', 3, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (17, '支付', '发票,开票,报销', '可以开发票吗？', '可以开具电子发票！\n\n开票流程：\n1. 订单完成后，进入订单详情\n2. 点击\"申请开票\"\n3. 填写发票抬头和税号\n4. 提交后 1-3 个工作日内发送至邮箱\n\n支持个人发票和企业发票。如有疑问，请联系客服。', 4, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (18, '支付', '支付失败,扣款成功但订单未生成', '支付失败了怎么办？', '如遇支付问题：\n\n1️⃣ 扣款成功但订单未生成：\n   - 等待 5 分钟，系统会自动对账\n   - 如仍未解决，联系客服人工处理\n   - 多扣款项会在 24 小时内退回\n\n2️⃣ 支付时提示失败：\n   - 检查网络连接\n   - 确认余额充足\n   - 尝试更换支付方式\n\n请放心，资金安全有保障！', 5, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (19, '支付', '会员,充值,积分', '有会员制度吗？', '我们暂未推出会员制度，但有以下优惠：\n\n🎁 新用户首单立减 ¥10\n🎁 满 ¥50 免配送费\n🎁 不定期发放优惠券\n🎁 邀请好友双方各得 ¥5\n\n关注公众号获取最新优惠活动信息！', 6, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (20, '菜品', '辣度,不辣,微辣,特辣', '可以调整菜品辣度吗？', '大部分菜品支持调整辣度！\n\n可在下单时选择：\n🌶️ 不辣\n🌶️ 微辣\n🌶️ 中辣\n🌶️ 重辣\n\n或在备注中说明具体要求，如\"一点辣都不要\"、\"越辣越好\"等，厨师会尽量满足您的需求。', 1, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (21, '菜品', '过敏,忌口,不要葱,不要香菜', '我对某些食材过敏怎么办？', '食品安全第一！\n\n下单时请：\n1. 在备注中明确标注过敏食材（如\"对花生过敏\"、\"不要香菜\"）\n2. 选择\"忌口\"标签（如果有）\n3. 如过敏严重，建议电话联系商家确认\n\n我们会认真处理您的特殊要求，但无法保证 100% 无交叉污染，请酌情选择。', 2, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (22, '菜品', '分量,够吃吗,几人份', '菜品分量怎么样？', '我们的菜品分量标准：\n\n🍜 单人餐：适合 1 人食用\n🍜 双人餐：适合 2 人食用\n🍜 家庭装：适合 3-4 人食用\n\n具体分量可在菜品详情页查看（标注克数或人数建议）。如不确定，可参考用户评价中的实拍图。', 3, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (23, '菜品', '新鲜,保质期,食材', '食材新鲜吗？', '我们严格把控食材质量：\n\n✅ 每日清晨采购新鲜食材\n✅ 蔬菜当日采购，肉类冷链运输\n✅ 厨房严格执行卫生标准\n✅ 定期检查食材保质期\n\n如您发现食材质量问题，请立即联系客服，我们会全额退款并严肃处理！', 4, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (24, '菜品', '推荐,特色,招牌,好吃', '有什么推荐的菜品？', '热门推荐：\n\n⭐ 招牌宫保鸡丁：酸甜微辣，下饭神器\n⭐ 水煮鱼：麻辣鲜香，鱼肉嫩滑\n⭐ 麻婆豆腐：经典川菜，入口即化\n⭐ 套餐A/B：性价比之王\n\n您也可以查看\"销量排行\"和\"用户好评\"，找到适合自己的美味！', 5, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (25, '菜品', '素食,清真,斋菜', '有素食或清真菜品吗？', '我们有部分素食菜品，可在搜索栏输入\"素\"字筛选。\n\n但目前暂无专门的清真认证菜品。如有特殊饮食需求，建议：\n1. 查看菜品配料表\n2. 联系商家确认制作工艺\n3. 选择明确的素食菜品\n\n我们会逐步丰富菜品类型，敬请期待！', 6, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (26, '售后', '投诉,举报,态度差', '如何投诉商家或骑手？', '我们非常重视您的反馈！\n\n投诉渠道：\n1. 订单详情页点击\"投诉\"\n2. 拨打客服电话：400-XXX-XXXX\n3. 在线客服留言\n\n请提供：\n- 订单号\n- 具体问题描述\n- 相关证据（照片/截图）\n\n我们会在 24 小时内处理并反馈结果！', 1, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (27, '售后', '漏送,少东西,没给', '收到的餐品有遗漏怎么办？', '非常抱歉！如遇漏送：\n\n1. 立即拍照留存证据\n2. 在订单页点击\"申请售后\"→\"漏送/错送\"\n3. 说明遗漏物品\n4. 选择退款或补送\n\n我们会快速处理，通常 30 分钟内给出解决方案。给您带来不便，深表歉意！', 2, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (28, '售后', '质量问题,坏了,变质,异物', '餐品有质量问题怎么处理？', '食品安全是我们的底线！\n\n如发现：\n- 食物变质、异味\n- 混入异物\n- 包装破损污染\n\n请立即：\n1. 拍照/录像取证\n2. 不要继续食用\n3. 联系客服（优先电话）\n4. 我们会全额退款并额外补偿\n\n同时会严肃调查商家，确保不再发生！', 3, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (29, '售后', '评价,打分,评论', '如何给订单评价？', '订单完成后可进行评价：\n\n1. 进入\"我的订单\"\n2. 找到已完成的订单\n3. 点击\"评价\"\n4. 选择星级（1-5星）\n5. 填写文字评价（可选）\n6. 上传实拍图（可选）\n\n您的评价对其他用户很有帮助，感谢支持！', 4, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (30, '售后', '差评,不满意,很差', '给了差评会有什么影响？', '您的每一条评价我们都很重视！\n\n差评后：\n- 商家会收到通知\n- 客服可能会联系您了解详情\n- 商家可能会主动联系您解决问题\n- 您可修改或删除评价（7天内）\n\n我们希望每位用户都满意，如有问题请先联系客服，我们会尽力解决！', 5, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (31, '售后', '赔偿,补偿,优惠券', '出现问题有赔偿吗？', '根据不同情况提供相应补偿：\n\n🔸 配送超时：¥3-5 优惠券\n🔸 漏送餐品：退还对应金额 + ¥5 券\n🔸 质量问题：全额退款 + ¥10-20 券\n🔸 严重问题：全额退款 + ¥20-50 券\n\n具体以客服处理结果为准。我们的目标是让您满意！', 6, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (32, '其他', '营业时间,几点开门,关门', '你们的营业时间是？', '我们的营业时间：\n\n🕐 早餐：07:00 - 09:30\n🕐 午餐：10:30 - 14:00\n🕐 晚餐：16:30 - 21:00\n🕐 夜宵：21:00 - 02:00（部分商家）\n\n具体以各商家实际营业时间为准，可在商家页面查看。', 1, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (33, '其他', '客服电话,人工,联系方式', '如何联系人工客服？', '联系客服方式：\n\n📞 电话客服：400-XXX-XXXX（9:00-22:00）\n💬 在线客服：APP内\"我的\"→\"客服中心\"\n📧 邮件客服：service@skytakeout.com\n\n高峰时段可能需要排队，请耐心等待。紧急问题建议电话咨询。', 2, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (34, '其他', '下载APP,小程序,在哪里点餐', '在哪里可以下单？', '您可以通过以下方式下单：\n\n📱 微信小程序：搜索\"苍穹外卖\"\n📱 APP：应用商店下载\"苍穹外卖\"\n💻 网页版：www.skytakeout.com（开发中）\n\n推荐使用小程序，无需下载，方便快捷！', 3, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (35, '其他', '注册,账号,登录', '如何注册账号？', '注册非常简单：\n\n1. 打开小程序或 APP\n2. 点击\"登录/注册\"\n3. 使用微信一键授权登录\n4. 自动完成注册\n\n无需填写手机号、密码等信息，3 秒搞定！后续可使用微信直接登录。', 4, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (36, '其他', '隐私,安全,信息泄露', '我的个人信息安全吗？', '我们高度重视用户隐私：\n\n🔒 数据加密存储\n🔒 严格遵守《个人信息保护法》\n🔒 不会向第三方出售用户数据\n🔒 骑手仅可见必要配送信息\n🔒 可随时注销账号删除数据\n\n隐私政策详见 APP 内\"设置\"→\"隐私政策\"。', 5, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (37, '其他', '加盟,合作,入驻', '如何成为商家或骑手？', '欢迎加入苍穹外卖！\n\n🏪 商家入驻：\n- 访问官网 www.skytakeout.com\n- 点击\"商家入驻\"\n- 提交资质材料\n- 审核通过后上线\n\n🚴 骑手招募：\n- 下载\"苍穹骑手\"APP\n- 注册并提交资料\n- 参加培训\n- 开始接单\n\n详询招商热线：400-XXX-XXXX', 6, 1, '2026-05-12 12:39:10', '2026-05-12 12:39:10');
INSERT INTO `customer_service_knowledge` VALUES (38, '其他', '你是谁,叫什么,名字,身份', '你是谁？', '您好！我是智能客服助手，可以为您解答关于配送、订单、支付、菜品等各种问题。请问有什么可以帮助您的？', 1, 1, '2026-05-12 13:29:45', '2026-05-12 13:29:45');
INSERT INTO `customer_service_knowledge` VALUES (39, '其他', '能干什么,功能,做什么,帮助', '你能干什么？', '我可以帮您：\n\n📦 查询订单状态和配送进度\n💰 解答支付和退款问题\n🍜 介绍菜品信息和推荐\n⏰ 咨询配送时间和范围\n🎁 了解优惠活动和优惠券\n📞 联系人工客服\n\n请问您想了解哪方面的信息？', 2, 1, '2026-05-12 13:29:45', '2026-05-12 13:29:45');
INSERT INTO `customer_service_knowledge` VALUES (40, '其他', '什么平台,介绍,干嘛的,做什么的', '这个平台是做什么的？', '我们是一个专业的在线外卖订餐平台，为您提供：\n\n🍜 丰富的菜品选择\n🚴 快速便捷的配送服务\n💳 安全可靠的支付方式\n⭐ 真实的用户评价体系\n\n我们致力于让每一餐都变得简单美好！', 3, 1, '2026-05-12 13:29:45', '2026-05-12 13:29:45');
INSERT INTO `customer_service_knowledge` VALUES (41, '其他', '怎么用,如何使用,新手,第一次', '第一次使用，怎么下单？', '下单非常简单：\n\n1️⃣ 浏览首页或搜索您想吃的菜品\n2️⃣ 将心仪的菜品加入购物车\n3️⃣ 点击\"去结算\"确认订单信息\n4️⃣ 选择收货地址和支付方式\n5️⃣ 提交订单并完成支付\n6️⃣ 等待美味送达！\n\n新用户还有首单立减 ¥10 优惠哦~', 4, 1, '2026-05-12 13:29:45', '2026-05-12 13:29:45');
INSERT INTO `customer_service_knowledge` VALUES (42, '其他', '修改手机号,换绑定,更换手机', '如何修改绑定的手机号？', '目前暂不支持自助修改手机号。\n\n如需修改，请联系人工客服：\n📞 电话：400-XXX-XXXX\n💬 在线客服：APP内\"我的\"→\"客服中心\"\n\n请准备好原手机号和新手机号，我们会为您快速处理。', 5, 1, '2026-05-12 13:29:45', '2026-05-12 13:29:45');
INSERT INTO `customer_service_knowledge` VALUES (43, '其他', '注销账号,删除账号,退出', '如何注销账号？', '注销账号流程：\n\n1. 进入\"我的\"→\"设置\"\n2. 点击\"账号与安全\"\n3. 选择\"注销账号\"\n4. 阅读注销须知并确认\n\n⚠️ 注意：\n- 注销后所有数据将无法恢复\n- 如有未完成订单，需先处理完毕\n- 注销申请将在 7 个工作日内审核\n\n如有疑问，请联系客服。', 6, 1, '2026-05-12 13:29:45', '2026-05-12 13:29:45');
INSERT INTO `customer_service_knowledge` VALUES (44, '配送', '加急,快点,催单,加速', '可以加急配送吗？', '我们已尽力优化配送效率，暂不提供付费加急服务。\n\n如您的订单确实紧急：\n1. 联系商家确认制作进度\n2. 联系骑手说明情况\n3. 在备注中标注\"加急\"\n\n我们会优先处理标注加急的订单，但无法保证具体时间，敬请谅解。', 7, 1, '2026-05-12 13:29:45', '2026-05-12 13:29:45');
INSERT INTO `customer_service_knowledge` VALUES (45, '配送', '自取,自己拿,到店取', '可以自己到店取餐吗？', '可以的！\n\n下单时选择\"自取\"方式：\n✅ 无需支付配送费\n✅ 可随时到店取餐\n✅ 支持提前预约取餐时间\n\n请在商家页面查看是否支持自取服务。部分商家可能仅支持配送。', 8, 1, '2026-05-12 13:29:45', '2026-05-12 13:29:45');
INSERT INTO `customer_service_knowledge` VALUES (46, '订单', '开发票,报销,电子发票', '如何申请开发票？', '开具发票流程：\n\n1. 订单完成后，进入订单详情\n2. 点击\"申请开票\"\n3. 填写发票信息：\n   - 发票抬头（个人/企业）\n   - 税号（企业必填）\n   - 接收邮箱\n4. 提交后 1-3 个工作日发送至邮箱\n\n支持增值税普通发票和专用发票。', 9, 1, '2026-05-12 13:29:45', '2026-05-12 13:29:45');
INSERT INTO `customer_service_knowledge` VALUES (47, '订单', '拼单,多人,一起买', '可以和 friends 一起拼单吗？', '目前暂不支持多人拼单功能。\n\n但您可以：\n1. 一人下单，添加多个菜品\n2. 分别下单，备注同一地址\n3. 使用\"购物车\"功能，邀请朋友选购后统一结算（开发中）\n\n我们会持续优化功能，敬请期待！', 10, 1, '2026-05-12 13:29:45', '2026-05-12 13:29:45');
INSERT INTO `customer_service_knowledge` VALUES (48, '支付', '余额,充值,钱包', '账户余额可以充值吗？', '目前暂不支持余额充值功能。\n\n我们提供以下支付方式：\n💳 微信支付\n💳 支付宝\n\n所有支付均通过正规渠道，资金安全有保障。未来可能会推出余额功能，敬请关注。', 11, 1, '2026-05-12 13:29:45', '2026-05-12 13:29:45');
INSERT INTO `customer_service_knowledge` VALUES (49, '支付', '分期付款,分期,花呗', '支持分期付款吗？', '目前暂不支持分期付款。\n\n订单金额需在下单时一次性支付完成。\n\n如金额较大，建议：\n- 使用信用卡支付（可享受银行分期）\n- 分多次下单\n- 等待平台推出分期功能\n\n感谢您的理解！', 12, 1, '2026-05-12 13:29:45', '2026-05-12 13:29:45');

-- ----------------------------
-- Table structure for dish
-- ----------------------------
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '菜品名称',
  `category_id` bigint NOT NULL COMMENT '菜品分类id',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '菜品价格',
  `image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '图片',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '描述信息',
  `status` int NULL DEFAULT 1 COMMENT '0 停售 1 起售',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_dish_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 87 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '菜品' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dish
-- ----------------------------
INSERT INTO `dish` VALUES (46, '王老吉', 11, 6.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', '', 1, '2022-06-09 22:40:47', '2025-12-13 17:16:06', 1, 0);
INSERT INTO `dish` VALUES (47, '北冰洋', 11, 4.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/65c90d7c-b5a1-4898-b885-5a1f2b3c1f2f.png', '还是小时候的味道', 1, '2022-06-10 09:18:49', '2025-12-13 15:20:12', 1, 0);
INSERT INTO `dish` VALUES (48, '雪花啤酒', 11, 4.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/7844fb98-51db-4f3b-97a8-71caa5913a63.png', '', 1, '2022-06-10 09:22:54', '2025-12-13 15:20:19', 1, 0);
INSERT INTO `dish` VALUES (49, '米饭', 12, 2.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/fc2262e7-1286-4d78-8ba3-a95aa5812c2e.png', '精选五常大米', 1, '2022-06-10 09:30:17', '2025-12-13 15:20:27', 1, 0);
INSERT INTO `dish` VALUES (50, '馒头', 12, 1.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/15d0aabc-4c49-4965-bdf6-4e6f6d7a1ca8.png', '优质面粉', 1, '2022-06-10 09:34:28', '2025-12-13 15:20:34', 1, 0);
INSERT INTO `dish` VALUES (51, '老坛酸菜鱼', 20, 56.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/1e58cbc5-3254-41ea-a3b8-c1d45ec450af.png', '原料：汤，草鱼，酸菜', 1, '2022-06-10 09:40:51', '2025-12-13 15:20:56', 1, 0);
INSERT INTO `dish` VALUES (52, '经典酸菜鮰鱼', 20, 66.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/811602ca-b916-4fc5-9d22-007efd125a7b.png', '原料：酸菜，江团，鮰鱼', 1, '2022-06-10 09:46:02', '2025-12-13 15:21:07', 1, 0);
INSERT INTO `dish` VALUES (53, '蜀味水煮草鱼', 20, 38.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/fb660a93-e61c-4ce2-8830-997ec1793165.png', '原料：草鱼，汤', 1, '2022-06-10 09:48:37', '2025-12-13 15:21:16', 1, 0);
INSERT INTO `dish` VALUES (54, '清炒小油菜', 19, 18.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/0d7c876d-0636-403e-8df6-af9a3c090c9b.png', '原料：小油菜', 1, '2022-06-10 09:51:46', '2025-12-13 15:21:23', 1, 0);
INSERT INTO `dish` VALUES (55, '蒜蓉娃娃菜', 19, 18.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/7c1ae12e-e228-4283-8808-02a4217d9b34.png', '原料：蒜，娃娃菜', 1, '2022-06-10 09:53:37', '2025-12-13 15:21:32', 1, 0);
INSERT INTO `dish` VALUES (56, '清炒西兰花', 19, 18.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/24a2245c-cc70-4886-8fc1-7ff1156b1ee1.png', '原料：西兰花', 1, '2022-06-10 09:55:44', '2025-12-13 15:21:43', 1, 0);
INSERT INTO `dish` VALUES (57, '炝炒圆白菜', 19, 18.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/6b07ea54-9f2b-4049-9a60-fe1e24eba5eb.png', '原料：圆白菜', 1, '2022-06-10 09:58:35', '2025-12-13 15:21:54', 1, 0);
INSERT INTO `dish` VALUES (58, '清蒸鲈鱼', 18, 98.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/0a967eb7-c6b2-42e7-85f1-ca5f8e1ca9da.png', '原料：鲈鱼', 1, '2022-06-10 10:12:28', '2025-12-13 15:22:04', 1, 0);
INSERT INTO `dish` VALUES (59, '东坡肘子', 18, 138.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/401198cd-5fa5-4a1e-8413-2fa860ae431a.png', '原料：猪肘棒', 1, '2022-06-10 10:24:03', '2025-12-13 15:22:12', 1, 0);
INSERT INTO `dish` VALUES (60, '梅菜扣肉', 18, 58.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/9b4f4b53-3aeb-433c-bd53-9c7ae7cfe1d7.png', '原料：猪肉，梅菜', 1, '2022-06-10 10:26:03', '2025-12-13 15:22:20', 1, 0);
INSERT INTO `dish` VALUES (61, '剁椒鱼头', 18, 66.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/d4d32305-d7cc-4985-bbea-ea945bad7dba.png', '原料：鲢鱼，剁椒', 1, '2022-06-10 10:28:54', '2025-12-13 15:22:28', 1, 0);
INSERT INTO `dish` VALUES (62, '金汤酸菜牛蛙', 17, 88.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/506ebbf8-2cb5-4f97-9995-dcb8cf1a7c3a.png', '原料：鲜活牛蛙，酸菜', 1, '2022-06-10 10:33:05', '2025-12-13 15:22:53', 1, 0);
INSERT INTO `dish` VALUES (63, '香锅牛蛙', 17, 88.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/7debe261-5d22-4715-9621-5536e3ffa4e2.png', '配料：鲜活牛蛙，莲藕，青笋', 1, '2022-06-10 10:35:40', '2025-12-13 15:23:02', 1, 0);
INSERT INTO `dish` VALUES (64, '馋嘴牛蛙', 17, 88.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/deb4e3c0-b523-418f-9ca6-8c6986c87986.png', '配料：鲜活牛蛙，丝瓜，黄豆芽', 1, '2022-06-10 10:37:52', '2025-12-13 15:23:11', 1, 0);
INSERT INTO `dish` VALUES (65, '草鱼2斤', 16, 68.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/cc103e7d-ac99-4790-9151-78039fedc8d7.png', '原料：草鱼，黄豆芽，莲藕', 1, '2022-06-10 10:41:08', '2025-12-13 15:23:20', 1, 0);
INSERT INTO `dish` VALUES (66, '江团鱼2斤', 16, 119.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/a10ab31b-0898-4084-8232-98ed4a07a794.png', '配料：江团鱼，黄豆芽，莲藕', 1, '2022-06-10 10:42:42', '2025-12-13 15:23:32', 1, 0);
INSERT INTO `dish` VALUES (67, '鮰鱼2斤', 16, 72.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/21a8e56a-f4fb-4a25-a7dd-739023f066bd.png', '原料：鮰鱼，黄豆芽，莲藕', 1, '2022-06-10 10:43:56', '2025-12-13 15:23:42', 1, 0);
INSERT INTO `dish` VALUES (68, '鸡蛋汤', 21, 4.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/4679ee45-ad27-493c-a51f-5e4ea5cd2083.png', '配料：鸡蛋，紫菜', 1, '2022-06-10 10:54:25', '2025-12-13 15:23:56', 1, 0);
INSERT INTO `dish` VALUES (69, '平菇豆腐汤', 21, 6.00, 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/5ade512a-9816-4561-9e08-7d5843228aa1.png', '配料：豆腐，平菇', 1, '2022-06-10 10:55:02', '2025-12-13 15:24:12', 1, 0);

-- ----------------------------
-- Table structure for dish_flavor
-- ----------------------------
DROP TABLE IF EXISTS `dish_flavor`;
CREATE TABLE `dish_flavor`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dish_id` bigint NOT NULL COMMENT '菜品',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '口味名称',
  `value` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '口味数据list',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 147 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '菜品口味关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dish_flavor
-- ----------------------------
INSERT INTO `dish_flavor` VALUES (40, 10, '甜味', '[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
INSERT INTO `dish_flavor` VALUES (41, 7, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (42, 7, '温度', '[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]');
INSERT INTO `dish_flavor` VALUES (45, 6, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (46, 6, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (47, 5, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (48, 5, '甜味', '[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
INSERT INTO `dish_flavor` VALUES (49, 2, '甜味', '[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
INSERT INTO `dish_flavor` VALUES (50, 4, '甜味', '[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
INSERT INTO `dish_flavor` VALUES (51, 3, '甜味', '[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
INSERT INTO `dish_flavor` VALUES (52, 3, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (135, 51, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (136, 51, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (137, 52, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (138, 52, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (139, 53, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (140, 53, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (141, 54, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\"]');
INSERT INTO `dish_flavor` VALUES (142, 56, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (143, 57, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (144, 60, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
INSERT INTO `dish_flavor` VALUES (145, 65, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (146, 66, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
INSERT INTO `dish_flavor` VALUES (147, 67, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '姓名',
  `username` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '密码',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '手机号',
  `sex` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '性别',
  `id_number` varchar(18) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '身份证号',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态 0:禁用，1:启用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '员工信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (0, '管理员', 'admin', 'password_md5_placeholder_admin', '13800000003', '1', '100000', 1, '2022-02-15 15:51:20', '2025-11-11 15:05:11', 0, 0);
INSERT INTO `employee` VALUES (1, '张三', '34204002', 'password_md5_placeholder_user', '13800000001', '男', '100001', 1, '2025-11-08 16:23:20', '2025-11-09 15:57:05', 0, 0);
INSERT INTO `employee` VALUES (8, '李四', '3196120036', 'password_md5_placeholder_admin', '13800000002', '男', '137891', 1, '2025-12-03 16:01:45', '2025-12-03 16:01:45', 1, 1);

-- ----------------------------
-- Table structure for marketing_case_library
-- ----------------------------
DROP TABLE IF EXISTS `marketing_case_library`;
CREATE TABLE `marketing_case_library`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dish_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜品分类（川菜/粤菜/快餐/套餐/饮品等）',
  `activity_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动类型（满减/折扣/新品上市/限时秒杀/组合套餐）',
  `channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '投放渠道（微信小程序/朋友圈/短信/抖音）',
  `style` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文案风格（活泼/温馨/专业/紧迫感）',
  `case_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '案例标题',
  `case_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文案内容',
  `character_count` int NULL DEFAULT NULL COMMENT '字数',
  `has_emoji` tinyint NULL DEFAULT 0 COMMENT '是否包含emoji（0否 1是）',
  `performance_score` decimal(3, 2) NULL DEFAULT 0.00 COMMENT '效果评分（0-5，基于点击率/转化率）',
  `usage_count` int NULL DEFAULT 0 COMMENT '使用次数',
  `conversion_rate` decimal(5, 4) NULL DEFAULT NULL COMMENT '转化率（可选，如有数据）',
  `compliance_notes` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合规注意事项',
  `tags` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签（逗号分隔，如：川味,特惠,限时）',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dish_category`(`dish_category` ASC) USING BTREE,
  INDEX `idx_activity_type`(`activity_type` ASC) USING BTREE,
  INDEX `idx_channel`(`channel` ASC) USING BTREE,
  INDEX `idx_style`(`style` ASC) USING BTREE,
  INDEX `idx_performance`(`performance_score` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '营销案例库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of marketing_case_library
-- ----------------------------
INSERT INTO `marketing_case_library` VALUES (1, '川菜', '满减', '微信小程序', '活泼', '川味特惠', '🔥川味来袭！宫保鸡丁+麻婆豆腐组合，满30减5，限时3天！经典川菜，下饭神器～', 48, 1, 4.50, 0, NULL, NULL, '川味,特惠,限时', 1, NULL, '2026-05-03 14:27:54', NULL, '2026-05-10 11:21:02');
INSERT INTO `marketing_case_library` VALUES (2, '川菜', '满减', '朋友圈', '温馨', '家的味道', '忙碌了一天，让经典川菜温暖你的胃。宫保鸡丁、麻婆豆腐满30减5，给自己一顿舒适的晚餐吧。', 52, 0, 4.20, 0, NULL, NULL, '温馨,家常菜,舒适', 1, NULL, '2026-05-03 14:27:55', NULL, '2026-05-10 11:22:26');
INSERT INTO `marketing_case_library` VALUES (3, '套餐', '组合优惠', '微信小程序', '紧迫感', '限时抢购', '⏰仅剩6小时！人气双人套餐原价68，现价58！手慢无，立即下单→', 42, 1, 4.80, 0, NULL, NULL, '限时,抢购,人气', 1, NULL, '2026-05-03 14:27:56', NULL, '2026-05-10 11:22:26');
INSERT INTO `marketing_case_library` VALUES (4, '饮品', '新品上市', '朋友圈', '活泼', '夏日清凉', '🧊新品首发！冰爽柠檬茶上线啦～清新解腻，搭配川菜绝配！前50名下单立减3元💰', 50, 1, 4.30, 0, NULL, NULL, '新品,清凉,解腻', 1, NULL, '2026-05-03 14:27:57', NULL, '2026-05-10 11:22:26');
INSERT INTO `marketing_case_library` VALUES (5, '快餐', '限时秒杀', '短信', '专业', '午市特惠', '【天外天】午市秒杀！11:00-13:00指定菜品5折起，配送费全免。退订回T', 45, 0, 3.90, 0, NULL, NULL, '秒杀,午市,免运费', 1, NULL, '2026-05-03 14:27:58', NULL, '2026-05-10 11:22:26');
INSERT INTO `marketing_case_library` VALUES (6, '酒水饮料', '折扣', '微信小程序', '活泼', '王老吉促销', '🌿王老吉，每一口都是匠心传承！🌿 精选草本原料，古法熬制工艺，入口甘醇回甜，清凉解暑又健康。6元就能喝到这份“草本精华”，是不是超值？✨ 不添加多余糖分，只保留自然草本香，喝得放心又过瘾。🍃 想体验这口地道凉茶味？小程序下单，把品质带回家！😋', 125, 1, 3.00, 0, 0.0000, 'AI生成，待验证', NULL, 1, 1, '2026-05-10 11:48:43', NULL, NULL);

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '名字',
  `image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '图片',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `dish_id` bigint NULL DEFAULT NULL COMMENT '菜品id',
  `setmeal_id` bigint NULL DEFAULT NULL COMMENT '套餐id',
  `dish_flavor` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '口味',
  `number` int NOT NULL DEFAULT 1 COMMENT '数量',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_detail
-- ----------------------------
INSERT INTO `order_detail` VALUES (5, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 6, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (6, '北冰洋', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/65c90d7c-b5a1-4898-b885-5a1f2b3c1f2f.png', 6, 47, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (7, '雪花啤酒', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/7844fb98-51db-4f3b-97a8-71caa5913a63.png', 6, 48, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (8, '剁椒鱼头', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/d4d32305-d7cc-4985-bbea-ea945bad7dba.png', 6, 61, NULL, NULL, 1, 66.00);
INSERT INTO `order_detail` VALUES (9, '套餐A', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/428a3573-e80b-45f2-9063-443a301e315e.png', 7, NULL, 38, NULL, 1, 110.00);
INSERT INTO `order_detail` VALUES (10, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 7, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (11, '北冰洋', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/65c90d7c-b5a1-4898-b885-5a1f2b3c1f2f.png', 7, 47, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (12, '米饭', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/fc2262e7-1286-4d78-8ba3-a95aa5812c2e.png', 7, 49, NULL, NULL, 2, 2.00);
INSERT INTO `order_detail` VALUES (13, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 8, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (14, '北冰洋', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/65c90d7c-b5a1-4898-b885-5a1f2b3c1f2f.png', 8, 47, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (15, '雪花啤酒', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/7844fb98-51db-4f3b-97a8-71caa5913a63.png', 8, 48, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (16, '草鱼2斤', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/cc103e7d-ac99-4790-9151-78039fedc8d7.png', 8, 65, NULL, NULL, 1, 68.00);
INSERT INTO `order_detail` VALUES (17, '雪花啤酒', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/7844fb98-51db-4f3b-97a8-71caa5913a63.png', 9, 48, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (18, '北冰洋', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/65c90d7c-b5a1-4898-b885-5a1f2b3c1f2f.png', 9, 47, NULL, NULL, 2, 4.00);
INSERT INTO `order_detail` VALUES (19, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 9, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (20, '大饭桶', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/067889f9-3d03-43d1-923c-3790e8a5b8d0.png', 9, NULL, 40, NULL, 1, 40.00);
INSERT INTO `order_detail` VALUES (21, '江团鱼2斤', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/a10ab31b-0898-4084-8232-98ed4a07a794.png', 9, 66, NULL, NULL, 1, 119.00);
INSERT INTO `order_detail` VALUES (22, '馋嘴牛蛙', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/deb4e3c0-b523-418f-9ca6-8c6986c87986.png', 9, 64, NULL, NULL, 1, 88.00);
INSERT INTO `order_detail` VALUES (23, '雪花啤酒', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/7844fb98-51db-4f3b-97a8-71caa5913a63.png', 10, 48, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (24, '北冰洋', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/65c90d7c-b5a1-4898-b885-5a1f2b3c1f2f.png', 10, 47, NULL, NULL, 2, 4.00);
INSERT INTO `order_detail` VALUES (25, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 10, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (26, '大饭桶', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/067889f9-3d03-43d1-923c-3790e8a5b8d0.png', 10, NULL, 40, NULL, 1, 40.00);
INSERT INTO `order_detail` VALUES (27, '江团鱼2斤', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/a10ab31b-0898-4084-8232-98ed4a07a794.png', 10, 66, NULL, NULL, 1, 119.00);
INSERT INTO `order_detail` VALUES (28, '馋嘴牛蛙', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/deb4e3c0-b523-418f-9ca6-8c6986c87986.png', 10, 64, NULL, NULL, 1, 88.00);
INSERT INTO `order_detail` VALUES (29, '雪花啤酒', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/7844fb98-51db-4f3b-97a8-71caa5913a63.png', 11, 48, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (30, '北冰洋', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/65c90d7c-b5a1-4898-b885-5a1f2b3c1f2f.png', 11, 47, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (31, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 11, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (32, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 12, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (33, '北冰洋', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/65c90d7c-b5a1-4898-b885-5a1f2b3c1f2f.png', 12, 47, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (34, '雪花啤酒', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/7844fb98-51db-4f3b-97a8-71caa5913a63.png', 12, 48, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (35, '剁椒鱼头', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/d4d32305-d7cc-4985-bbea-ea945bad7dba.png', 12, 61, NULL, NULL, 1, 66.00);
INSERT INTO `order_detail` VALUES (36, '馋嘴牛蛙', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/deb4e3c0-b523-418f-9ca6-8c6986c87986.png', 13, 64, NULL, NULL, 1, 88.00);
INSERT INTO `order_detail` VALUES (37, '香锅牛蛙', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/7debe261-5d22-4715-9621-5536e3ffa4e2.png', 13, 63, NULL, NULL, 1, 88.00);
INSERT INTO `order_detail` VALUES (38, '金汤酸菜牛蛙', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/506ebbf8-2cb5-4f97-9995-dcb8cf1a7c3a.png', 13, 62, NULL, NULL, 1, 88.00);
INSERT INTO `order_detail` VALUES (39, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 14, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (40, '江团鱼2斤', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/a10ab31b-0898-4084-8232-98ed4a07a794.png', 14, 66, NULL, NULL, 1, 119.00);
INSERT INTO `order_detail` VALUES (41, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 15, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (42, '江团鱼2斤', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/a10ab31b-0898-4084-8232-98ed4a07a794.png', 15, 66, NULL, NULL, 1, 119.00);
INSERT INTO `order_detail` VALUES (43, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 16, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (44, '北冰洋', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/65c90d7c-b5a1-4898-b885-5a1f2b3c1f2f.png', 16, 47, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (45, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 17, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (46, '江团鱼2斤', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/a10ab31b-0898-4084-8232-98ed4a07a794.png', 17, 66, NULL, NULL, 1, 119.00);
INSERT INTO `order_detail` VALUES (47, '蜀味水煮草鱼', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/fb660a93-e61c-4ce2-8830-997ec1793165.png', 18, 53, NULL, NULL, 1, 38.00);
INSERT INTO `order_detail` VALUES (48, '经典酸菜鮰鱼', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/811602ca-b916-4fc5-9d22-007efd125a7b.png', 18, 52, NULL, NULL, 1, 66.00);
INSERT INTO `order_detail` VALUES (49, '老坛酸菜鱼', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/1e58cbc5-3254-41ea-a3b8-c1d45ec450af.png', 18, 51, NULL, NULL, 1, 56.00);
INSERT INTO `order_detail` VALUES (50, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 19, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (51, '江团鱼2斤', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/a10ab31b-0898-4084-8232-98ed4a07a794.png', 19, 66, NULL, NULL, 1, 119.00);
INSERT INTO `order_detail` VALUES (52, '经典酸菜鮰鱼', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/811602ca-b916-4fc5-9d22-007efd125a7b.png', 20, 52, NULL, NULL, 1, 66.00);
INSERT INTO `order_detail` VALUES (53, '北冰洋', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/65c90d7c-b5a1-4898-b885-5a1f2b3c1f2f.png', 21, 47, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (54, '套餐A', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/428a3573-e80b-45f2-9063-443a301e315e.png', 21, NULL, 38, NULL, 1, 110.00);
INSERT INTO `order_detail` VALUES (55, '套餐B', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/eaed72cb-0b70-41c3-9340-56b67189b05b.png', 21, NULL, 39, NULL, 1, 89.90);
INSERT INTO `order_detail` VALUES (56, '大饭桶', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/067889f9-3d03-43d1-923c-3790e8a5b8d0.png', 22, NULL, 40, NULL, 11, 40.00);
INSERT INTO `order_detail` VALUES (57, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 22, 46, NULL, NULL, 2, 6.00);
INSERT INTO `order_detail` VALUES (58, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 23, 46, NULL, NULL, 2, 6.00);
INSERT INTO `order_detail` VALUES (59, '套餐A', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/428a3573-e80b-45f2-9063-443a301e315e.png', 23, NULL, 38, NULL, 1, 110.00);
INSERT INTO `order_detail` VALUES (60, '套餐B', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/eaed72cb-0b70-41c3-9340-56b67189b05b.png', 23, NULL, 39, NULL, 1, 89.90);
INSERT INTO `order_detail` VALUES (61, '梅菜扣肉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/9b4f4b53-3aeb-433c-bd53-9c7ae7cfe1d7.png', 24, 60, NULL, NULL, 1, 58.00);
INSERT INTO `order_detail` VALUES (62, '剁椒鱼头', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/d4d32305-d7cc-4985-bbea-ea945bad7dba.png', 24, 61, NULL, NULL, 1, 66.00);
INSERT INTO `order_detail` VALUES (63, '东坡肘子', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/401198cd-5fa5-4a1e-8413-2fa860ae431a.png', 24, 59, NULL, NULL, 1, 138.00);
INSERT INTO `order_detail` VALUES (64, '米饭', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/fc2262e7-1286-4d78-8ba3-a95aa5812c2e.png', 24, 49, NULL, NULL, 1, 2.00);
INSERT INTO `order_detail` VALUES (65, '梅菜扣肉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/9b4f4b53-3aeb-433c-bd53-9c7ae7cfe1d7.png', 25, 60, NULL, NULL, 1, 58.00);
INSERT INTO `order_detail` VALUES (66, '剁椒鱼头', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/d4d32305-d7cc-4985-bbea-ea945bad7dba.png', 25, 61, NULL, NULL, 1, 66.00);
INSERT INTO `order_detail` VALUES (67, '东坡肘子', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/401198cd-5fa5-4a1e-8413-2fa860ae431a.png', 25, 59, NULL, NULL, 1, 138.00);
INSERT INTO `order_detail` VALUES (68, '米饭', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/fc2262e7-1286-4d78-8ba3-a95aa5812c2e.png', 25, 49, NULL, NULL, 1, 2.00);
INSERT INTO `order_detail` VALUES (69, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 26, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (70, '北冰洋', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/65c90d7c-b5a1-4898-b885-5a1f2b3c1f2f.png', 26, 47, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (71, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 27, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (72, '北冰洋', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/65c90d7c-b5a1-4898-b885-5a1f2b3c1f2f.png', 27, 47, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (73, '王老吉', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/c2ab2d51-69fc-4910-a16a-61bef0db5e2d.png', 28, 46, NULL, NULL, 1, 6.00);
INSERT INTO `order_detail` VALUES (74, '北冰洋', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/65c90d7c-b5a1-4898-b885-5a1f2b3c1f2f.png', 28, 47, NULL, NULL, 1, 4.00);
INSERT INTO `order_detail` VALUES (75, '套餐A', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/428a3573-e80b-45f2-9063-443a301e315e.png', 29, NULL, 38, NULL, 1, 110.00);
INSERT INTO `order_detail` VALUES (76, '套餐A', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/428a3573-e80b-45f2-9063-443a301e315e.png', 30, NULL, 38, NULL, 1, 110.00);

-- ----------------------------
-- Table structure for order_remark_tag
-- ----------------------------
DROP TABLE IF EXISTS `order_remark_tag`;
CREATE TABLE `order_remark_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `flavor_tags` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '口味标签（JSON数组）[\"不辣\",\"多醋\",\"不要葱\"]',
  `delivery_tags` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配送标签（JSON数组）[\"放门卫\",\"打电话\"]',
  `urgency_level` tinyint NULL DEFAULT 0 COMMENT '紧急程度（0普通 1加急 2特急）',
  `original_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原始备注文本',
  `parse_status` tinyint NULL DEFAULT 1 COMMENT '解析状态（1成功 2失败 3人工审核）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_urgency`(`urgency_level` ASC) USING BTREE,
  INDEX `idx_parse_status`(`parse_status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单备注标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_remark_tag
-- ----------------------------
INSERT INTO `order_remark_tag` VALUES (1, 27, '[\"免辣\",\"不要香菜\"]', '[\"挂门把手\"]', 1, '吃不了辣，不喜欢香菜，多加点葱，挂门上，尽量快点送到。', 1, '2026-05-04 11:28:29', '2026-05-04 11:28:29');
INSERT INTO `order_remark_tag` VALUES (2, 28, '[\"不辣\",\"不要香菜\"]', '[\"挂门把手\"]', 0, '吃不了辣，外卖挂门上就好，不喜欢香菜。', 1, '2026-05-04 11:37:59', '2026-05-04 11:37:59');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '订单号',
  `status` int NOT NULL DEFAULT 1 COMMENT '订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款',
  `user_id` bigint NOT NULL COMMENT '下单用户',
  `address_book_id` bigint NOT NULL COMMENT '地址id',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `checkout_time` datetime NULL DEFAULT NULL COMMENT '结账时间',
  `pay_method` int NOT NULL DEFAULT 1 COMMENT '支付方式 1微信,2支付宝',
  `pay_status` tinyint NOT NULL DEFAULT 0 COMMENT '支付状态 0未支付 1已支付 2退款',
  `amount` decimal(10, 2) NOT NULL COMMENT '实收金额',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '备注',
  `remark_flavor` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '口味标签（JSON数组）[\"不辣\",\"多醋\"]',
  `remark_delivery` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '配送标签（JSON数组）[\"放门卫\",\"打电话\"]',
  `remark_urgency` tinyint NULL DEFAULT 0 COMMENT '紧急程度（0普通 1加急 2特急）',
  `ai_parse_status` tinyint NULL DEFAULT 0 COMMENT 'AI解析状态（0未解析 1已解析 2解析失败）',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '手机号',
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '地址',
  `user_name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '用户名称',
  `consignee` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '收货人',
  `cancel_reason` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '订单取消原因',
  `rejection_reason` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '订单拒绝原因',
  `cancel_time` datetime NULL DEFAULT NULL COMMENT '订单取消时间',
  `estimated_delivery_time` datetime NULL DEFAULT NULL COMMENT '预计送达时间',
  `delivery_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '配送状态  1立即送出  0选择具体时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '送达时间',
  `pack_amount` int NULL DEFAULT NULL COMMENT '打包费',
  `tableware_number` int NULL DEFAULT NULL COMMENT '餐具数量',
  `tableware_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '餐具数量状态  1按餐量提供  0选择具体数量',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_remark_urgency`(`remark_urgency` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (6, '202601171712583394', 6, 4, 3, '2026-01-17 17:12:58', NULL, 1, 2, 80.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', '这么久不付钱别吃了', NULL, '2026-01-19 15:56:29', '2026-01-17 18:12:00', 0, NULL, 4, 0, 0);
INSERT INTO `orders` VALUES (7, '202601171831557824', 6, 4, 3, '2026-01-17 18:31:56', NULL, 1, 2, 124.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', '这么久不付钱别吃了', NULL, '2026-01-19 15:56:25', '2026-01-17 19:31:00', 0, NULL, 5, 0, 0);
INSERT INTO `orders` VALUES (8, '202601181121324314', 6, 4, 2, '2026-01-18 11:21:32', '2026-01-18 11:21:45', 1, 2, 82.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', '用户取消', NULL, '2026-01-18 15:03:35', '2026-01-18 12:21:00', 0, NULL, 4, 0, 0);
INSERT INTO `orders` VALUES (9, '202601181127347834', 6, 4, 3, '2026-01-18 11:27:35', '2026-01-18 11:27:38', 1, 1, 265.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, '不想卖了', '2026-01-18 15:03:35', '2026-01-18 12:27:00', 0, NULL, 7, 0, 0);
INSERT INTO `orders` VALUES (10, '202601181504319174', 5, 4, 3, '2026-01-18 15:04:32', '2026-01-18 15:04:35', 1, 1, 265.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, NULL, NULL, '2026-01-18 16:04:00', 0, '2026-01-19 15:52:11', 7, 0, 0);
INSERT INTO `orders` VALUES (11, '202601181508065194', 6, 4, 3, '2026-01-18 15:08:07', '2026-01-18 15:08:08', 1, 2, 14.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', '不想卖了', NULL, '2026-01-19 15:52:00', '2026-01-18 16:08:00', 0, NULL, 3, 0, 0);
INSERT INTO `orders` VALUES (12, '202601181509508274', 6, 4, 3, '2026-01-18 15:09:51', '2026-01-18 15:09:52', 1, 2, 80.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', '用户取消', NULL, '2026-01-18 15:10:26', '2026-01-18 16:09:00', 0, NULL, 4, 0, 0);
INSERT INTO `orders` VALUES (13, '202601201508128024', 5, 4, 3, '2026-01-20 15:08:13', '2026-01-20 15:08:14', 1, 1, 264.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, NULL, NULL, '2026-01-20 16:08:00', 0, '2026-01-20 17:40:00', 3, 0, 0);
INSERT INTO `orders` VALUES (14, '202601201508260534', 6, 4, 3, '2026-01-20 15:08:26', NULL, 1, 0, 125.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', '支付超时', NULL, '2026-01-20 15:24:00', '2026-01-20 16:08:00', 0, NULL, 2, 0, 0);
INSERT INTO `orders` VALUES (15, '202601201823253974', 6, 4, 3, '2026-01-20 18:23:25', '2026-01-20 18:23:27', 1, 1, 125.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, '不想卖了', NULL, '2026-01-20 19:23:00', 0, NULL, 2, 0, 0);
INSERT INTO `orders` VALUES (16, '202601201823562064', 6, 4, 3, '2026-01-20 18:23:56', '2026-01-20 18:23:58', 1, 2, 10.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', '用户取消', NULL, '2026-01-20 18:24:47', '2026-01-20 19:23:00', 0, NULL, 2, 0, 0);
INSERT INTO `orders` VALUES (17, '202601201824588544', 6, 4, 3, '2026-01-20 18:24:59', '2026-01-20 18:25:01', 1, 1, 125.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, '不想卖了', NULL, '2026-01-20 19:24:00', 0, NULL, 2, 0, 0);
INSERT INTO `orders` VALUES (18, '202601211149395634', 6, 4, 3, '2026-01-21 11:49:40', '2026-01-21 11:49:41', 1, 2, 160.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', '不想给你吃了', NULL, '2026-01-21 11:52:39', '2026-01-21 12:49:00', 0, NULL, 3, 0, 0);
INSERT INTO `orders` VALUES (19, '202601211629362454', 5, 4, 3, '2026-01-21 16:29:36', '2026-01-21 16:29:38', 1, 1, 125.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, NULL, NULL, '2026-01-21 17:29:00', 0, '2026-01-21 16:31:16', 2, 0, 0);
INSERT INTO `orders` VALUES (20, '202601211629503144', 5, 4, 3, '2026-01-21 16:29:50', '2026-01-21 16:29:52', 1, 1, 66.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, NULL, NULL, '2026-01-21 17:29:00', 0, '2026-01-21 16:31:16', 1, 0, 0);
INSERT INTO `orders` VALUES (21, '202601211631026834', 5, 4, 3, '2026-01-21 16:31:03', '2026-01-21 16:31:04', 1, 1, 203.90, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, NULL, NULL, '2026-01-21 17:30:00', 0, '2026-01-21 16:31:15', 3, 0, 0);
INSERT INTO `orders` VALUES (22, '202601221728236024', 5, 4, 3, '2026-01-21 17:28:24', '2026-01-21 17:28:25', 1, 1, 452.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, NULL, NULL, '2026-01-21 18:28:00', 0, '2026-01-21 17:28:55', 13, 0, 0);
INSERT INTO `orders` VALUES (23, '202601221854340254', 5, 4, 3, '2026-01-22 18:54:34', '2026-01-22 18:54:41', 1, 1, 211.90, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, NULL, NULL, '2026-01-22 19:54:00', 0, '2026-01-22 18:54:56', 4, 0, 0);
INSERT INTO `orders` VALUES (24, '202601221855244504', 5, 4, 3, '2026-01-22 18:55:24', '2026-01-22 18:55:26', 1, 1, 264.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, NULL, NULL, '2026-01-22 19:55:00', 0, '2026-01-22 18:55:36', 4, 0, 0);
INSERT INTO `orders` VALUES (25, '202601221855552564', 6, 4, 3, '2026-01-22 18:55:55', '2026-01-22 18:55:57', 1, 2, 264.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', '客户电话取消', NULL, '2026-01-22 18:56:10', '2026-01-22 19:55:00', 0, NULL, 4, 0, 0);
INSERT INTO `orders` VALUES (26, '202605041125571874', 6, 4, 3, '2026-05-04 11:25:57', '2026-05-04 11:25:57', 1, 2, 10.00, '', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', '用户取消', NULL, '2026-05-04 11:26:11', '2026-05-04 12:25:00', 0, NULL, 2, 0, 0);
INSERT INTO `orders` VALUES (27, '202605041128012584', 6, 4, 3, '2026-05-04 11:28:01', '2026-05-04 11:28:03', 1, 2, 10.00, '吃不了辣，不喜欢香菜，多加点葱，挂门上，尽量快点送到。', NULL, NULL, 0, 0, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', '用户取消', NULL, '2026-05-04 11:36:57', '2026-05-04 12:27:00', 0, NULL, 2, 0, 0);
INSERT INTO `orders` VALUES (28, '202605041137360194', 5, 4, 3, '2026-05-04 11:37:36', '2026-05-04 11:37:38', 1, 1, 10.00, '吃不了辣，外卖挂门上就好，不喜欢香菜。', '["不辣","不要香菜"]', '["挂门把手"]', 0, 1, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, NULL, NULL, '2026-05-04 12:37:00', 0, '2026-05-05 17:02:41', 2, 0, 0);
INSERT INTO `orders` VALUES (29, '202605081548348734', 5, 4, 3, '2026-05-08 15:48:35', '2026-05-08 15:48:36', 1, 1, 110.00, '', NULL, NULL, NULL, NULL, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, NULL, NULL, '2026-05-08 16:48:00', 0, '2026-05-08 15:49:03', 1, 0, 0);
INSERT INTO `orders` VALUES (30, '202605081623205214', 5, 4, 3, '2026-05-08 16:23:21', '2026-05-08 16:23:22', 1, 1, 110.00, '', NULL, NULL, NULL, NULL, '13800000001', '江西省某某市某某县某某街道', '张', 'user001', NULL, NULL, NULL, '2026-05-08 17:23:00', 0, '2026-05-08 16:23:47', 1, 0, 0);

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `rating` int NOT NULL COMMENT '评分（1-5星）',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评价内容',
  `reply_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商家回复内容',
  `reply_time` datetime NULL DEFAULT NULL COMMENT '回复时间',
  `status` int NOT NULL DEFAULT 1 COMMENT '评价状态（0待审核 1已发布 2已隐藏）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '店铺评价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review` VALUES (14, 28, 4, 2, '饭有点少，不够吃。', '尊敬的顾客，感谢您的反馈。关于“饭量偏少”的问题，我们已记录并会与厨房沟通调整分量标准。建议您下次下单时选择“加量”选项，或联系店员免费加饭。期待为您提供更满意的用餐体验。', '2026-05-08 15:38:18', 1, '2026-05-08 15:34:27', '2026-05-08 15:38:18');
INSERT INTO `review` VALUES (15, 29, 4, 4, '好吃，饭菜还不错呢，就是量有点少了，下次菜能多点就好了。', '尊敬的顾客，感谢您的肯定与建议。关于“菜量偏少”的问题我们非常重视，已记录并会与厨房沟通优化分量标准。欢迎您下次下单时选择“加量”选项，或直接告知店员调整。期待为您带来更满足的用餐体验！', '2026-05-08 15:52:41', 1, '2026-05-08 15:50:12', '2026-05-08 15:52:40');
INSERT INTO `review` VALUES (16, 30, 4, 3, '还可以，就是吃的有点腻了，商家来点新花样呗。', '尊敬的顾客，感谢您的宝贵反馈！我们非常重视您提出的口味偏腻的问题，后续会结合菜品搭配和调味进行创新尝试，比如增加清爽小菜或调整烹饪方式。期待为您带来更丰富、更清爽的用餐体验！再次感谢您的建议！', '2026-05-08 16:26:36', 1, '2026-05-08 16:24:44', '2026-05-08 16:26:35');

-- ----------------------------
-- Table structure for review_reply_draft
-- ----------------------------
DROP TABLE IF EXISTS `review_reply_draft`;
CREATE TABLE `review_reply_draft`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `review_id` bigint NULL DEFAULT NULL COMMENT '评价ID（关联评价表，如果有的话）',
  `review_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '用户差评原文',
  `ai_analysis` json NULL COMMENT 'AI分析结果 {\"emotion_level\":\"愤怒\",\"problem_type\":\"配送超时\",\"core_complaint\":\"...\",\"user_demand\":\"...\"}',
  `retrieved_templates` json NULL COMMENT 'RAG召回的模板列表 [{\"id\":1,\"template\":\"...\",\"similarity\":0.85},...]',
  `generated_reply` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'AI生成的回复内容',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'pending_review' COMMENT '状态（pending_review待审核/approved已通过/rejected已拒绝/modified已修改）',
  `merchant_modified_reply` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商家修改后的回复',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `ai_model` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '使用的AI模型',
  `token_usage` int NULL DEFAULT NULL COMMENT 'Token消耗量',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人（AI生成时为系统ID）',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新人（商家审核时记录）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '差评回复草稿表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review_reply_draft
-- ----------------------------
INSERT INTO `review_reply_draft` VALUES (1, 28, 14, '饭有点少，不够吃。', '{\"type\": \"菜品质量\", \"demand\": \"改进\", \"emotion\": \"中性\"}', '[]', '尊敬的顾客，感谢您的反馈。关于“饭量偏少”的问题，我们已记录并会与厨房沟通调整分量标准。建议您下次下单时选择“加量”选项，或联系店员免费加饭。期待为您提供更满意的用餐体验。', 'approved', NULL, '2026-05-08 15:38:18', 'deepseek-ai/DeepSeek-V4-Flash', 0, '2026-05-08 15:34:34', '2026-05-08 15:38:18', NULL, NULL);
INSERT INTO `review_reply_draft` VALUES (2, 29, 15, '好吃，饭菜还不错呢，就是量有点少了，下次菜能多点就好了。', '{\"type\": \"菜品质量\", \"demand\": \"改进\", \"emotion\": \"满意\"}', '[{\"id\": null, \"status\": null, \"category\": \"historical_reply\", \"keywords\": null, \"useCount\": null, \"createTime\": null, \"createUser\": null, \"updateTime\": null, \"updateUser\": null, \"subCategory\": null, \"emotionLevel\": null, \"replyTemplate\": \"饭有点少，不够吃。 [回复] 尊敬的顾客，感谢您的反馈。关于“饭量偏少”的问题，我们已记录并会与厨房沟通调整分量标准。建议您下次下单时选择“加量”选项，或联系店员免费加饭。期待为您提供更满意的用餐体验。\", \"complianceRules\": null, \"compensationType\": null, \"effectivenessScore\": null}]', '尊敬的顾客，感谢您的认可与宝贵建议。关于菜品分量不足的问题，我们已记录并会与厨房沟通优化。建议您下次下单时选择“大份”选项，或联系店员加量。期待为您提供更满意的用餐体验！', 'rejected', NULL, NULL, 'deepseek-ai/DeepSeek-V4-Flash', 0, '2026-05-08 15:50:19', '2026-05-08 15:51:23', NULL, NULL);
INSERT INTO `review_reply_draft` VALUES (3, 29, 15, '好吃，饭菜还不错呢，就是量有点少了，下次菜能多点就好了。', '{\"type\": \"菜品质量\", \"demand\": \"改进\", \"emotion\": \"满意\"}', '[{\"id\": null, \"status\": null, \"category\": \"historical_reply\", \"keywords\": null, \"useCount\": null, \"createTime\": null, \"createUser\": null, \"updateTime\": null, \"updateUser\": null, \"subCategory\": null, \"emotionLevel\": null, \"replyTemplate\": \"饭有点少，不够吃。 [回复] 尊敬的顾客，感谢您的反馈。关于“饭量偏少”的问题，我们已记录并会与厨房沟通调整分量标准。建议您下次下单时选择“加量”选项，或联系店员免费加饭。期待为您提供更满意的用餐体验。\", \"complianceRules\": null, \"compensationType\": null, \"effectivenessScore\": null}]', '尊敬的顾客，感谢您的肯定与建议。关于“菜量偏少”的问题我们非常重视，已记录并会与厨房沟通优化分量标准。欢迎您下次下单时选择“加量”选项，或直接告知店员调整。期待为您带来更满足的用餐体验！', 'approved', NULL, '2026-05-08 15:52:41', 'deepseek-ai/DeepSeek-V4-Flash', 0, '2026-05-08 15:51:30', '2026-05-08 15:52:40', NULL, NULL);
INSERT INTO `review_reply_draft` VALUES (4, 30, 16, '还可以，就是吃的有点腻了，商家来点新花样呗。', '{\"type\": \"口味问题\", \"demand\": \"改进\", \"emotion\": \"中性\"}', '[]', '尊敬的顾客，感谢您的宝贵反馈！我们非常重视您提出的口味偏腻的问题，后续会结合菜品搭配和调味进行创新尝试，比如增加清爽小菜或调整烹饪方式。期待为您带来更丰富、更清爽的用餐体验！再次感谢您的建议！', 'approved', NULL, '2026-05-08 16:26:36', 'deepseek-ai/DeepSeek-V4-Flash', 0, '2026-05-08 16:24:50', '2026-05-08 16:26:35', NULL, NULL);

-- ----------------------------
-- Table structure for review_reply_knowledge
-- ----------------------------
DROP TABLE IF EXISTS `review_reply_knowledge`;
CREATE TABLE `review_reply_knowledge`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题分类（配送超时/菜品质量/服务态度/包装问题/其他）',
  `sub_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子分类（如：配送超时下的\"骑手态度\"\"送达延误\"）',
  `keywords` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关键词（逗号分隔，用于传统匹配）',
  `reply_template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回复模板',
  `compliance_rules` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合规规则（必须包含的内容、禁用词汇等）',
  `emotion_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '适用情绪等级（轻微不满/愤怒/非常愤怒）',
  `compensation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '补偿类型（退款/优惠券/积分/赠品）',
  `use_count` int NULL DEFAULT 0 COMMENT '使用次数',
  `effectiveness_score` decimal(3, 2) NULL DEFAULT 0.00 COMMENT '效果评分（0-5，基于商家采纳率和用户满意度）',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE,
  INDEX `idx_sub_category`(`sub_category` ASC) USING BTREE,
  INDEX `idx_emotion`(`emotion_level` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_effectiveness`(`effectiveness_score` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '差评回复知识库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review_reply_knowledge
-- ----------------------------
INSERT INTO `review_reply_knowledge` VALUES (1, '配送超时', '送达延误', '超时,延误,慢,很久', '非常抱歉给您带来不好的体验！由于{原因}，导致配送延误。我们已为您{补偿方案}。我们会加强骑手培训，优化配送流程，避免类似情况再次发生。感谢您的理解与支持！', '必须包含：道歉+原因说明+补偿方案+改进措施；禁止使用：\"可能\"\"也许\"\"大概\"等模糊词汇', '愤怒', '退款', 0, 4.50, 1, NULL, '2026-05-03 14:19:34', NULL, '2026-05-03 14:19:34');
INSERT INTO `review_reply_knowledge` VALUES (2, '配送超时', '骑手态度', '态度差,凶,不耐烦', '对于骑手的服务态度问题，我们深表歉意！这严重违反了我们的服务标准。我们已对涉事骑手进行处罚，并将加强培训。为表歉意，我们为您{补偿方案}。欢迎您继续监督我们的服务！', '必须包含：诚恳道歉+处理措施+补偿方案；禁止推卸责任', '非常愤怒', '优惠券', 0, 4.20, 1, NULL, '2026-05-03 14:19:34', NULL, '2026-05-03 14:19:34');
INSERT INTO `review_reply_knowledge` VALUES (3, '菜品质量', '口味问题', '难吃,不好吃,味道怪,咸,淡', '非常抱歉菜品未能满足您的口味！我们会立即反馈给厨师团队，调整制作工艺。为表歉意，我们为您{补偿方案}。希望您能给我们一次改进的机会，下次一定让您满意！', '必须包含：道歉+改进承诺+补偿方案；禁止质疑用户品味', '轻微不满', '赠品', 0, 4.00, 1, NULL, '2026-05-03 14:19:34', NULL, '2026-05-03 14:19:34');
INSERT INTO `review_reply_knowledge` VALUES (4, '菜品质量', '食材新鲜度', '不新鲜,变质,坏了,异味', '对于食材质量问题，我们高度重视并深表歉意！这严重违反了我们的食品安全标准。我们已下架相关食材并全面排查。为您全额退款并{补偿方案}。欢迎您随时监督我们的食品安全！', '必须包含：严肃道歉+处理措施+全额退款+额外补偿；必须体现重视程度', '非常愤怒', '退款', 0, 4.80, 1, NULL, '2026-05-03 14:19:34', NULL, '2026-05-03 14:19:34');
INSERT INTO `review_reply_knowledge` VALUES (5, '服务态度', '客服响应慢', '没人理,不回消息,慢', '抱歉让您久等了！由于咨询量较大，我们未能及时响应。我们已增加客服人员，优化响应流程。为表歉意，我们为您{补偿方案}。请问还有什么可以帮助您的吗？', '必须包含：道歉+原因说明+改进措施+补偿方案', '愤怒', '优惠券', 0, 3.90, 1, NULL, '2026-05-03 14:19:34', NULL, '2026-05-03 14:19:34');
INSERT INTO `review_reply_knowledge` VALUES (6, '包装问题', '洒漏', '洒了,漏了,破了,损坏', '非常抱歉包装出现问题的！我们会立即检查包装流程，升级包装材料。为您{补偿方案}。感谢您的反馈，帮助我们不断改进！', '必须包含：道歉+改进措施+补偿方案', '轻微不满', '退款', 0, 4.10, 1, NULL, '2026-05-03 14:19:34', NULL, '2026-05-03 14:19:34');

-- ----------------------------
-- Table structure for setmeal
-- ----------------------------
DROP TABLE IF EXISTS `setmeal`;
CREATE TABLE `setmeal`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint NOT NULL COMMENT '菜品分类id',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '套餐名称',
  `price` decimal(10, 2) NOT NULL COMMENT '套餐价格',
  `status` int NULL DEFAULT 1 COMMENT '售卖状态 0:停售 1:起售',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '描述信息',
  `image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '图片',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_setmeal_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '套餐' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of setmeal
-- ----------------------------
INSERT INTO `setmeal` VALUES (38, 13, '套餐A', 110.00, 1, '', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/428a3573-e80b-45f2-9063-443a301e315e.png', '2025-12-13 15:28:58', '2025-12-16 16:46:45', 0, 0);
INSERT INTO `setmeal` VALUES (39, 13, '套餐B', 89.90, 1, '', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/eaed72cb-0b70-41c3-9340-56b67189b05b.png', '2025-12-13 15:30:00', '2025-12-13 15:30:07', 0, 0);
INSERT INTO `setmeal` VALUES (40, 15, '大饭桶', 40.00, 1, '大胃袋套餐', 'https://jiang-learning.oss-cn-beijing.aliyuncs.com/067889f9-3d03-43d1-923c-3790e8a5b8d0.png', '2025-12-16 15:45:59', '2025-12-16 15:46:11', 0, 0);

-- ----------------------------
-- Table structure for setmeal_dish
-- ----------------------------
DROP TABLE IF EXISTS `setmeal_dish`;
CREATE TABLE `setmeal_dish`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `setmeal_id` bigint NULL DEFAULT NULL COMMENT '套餐id',
  `dish_id` bigint NULL DEFAULT NULL COMMENT '菜品id',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '菜品名称 （冗余字段）',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '菜品单价（冗余字段）',
  `copies` int NULL DEFAULT NULL COMMENT '菜品份数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 111 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '套餐菜品关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of setmeal_dish
-- ----------------------------
INSERT INTO `setmeal_dish` VALUES (66, 39, 68, '鸡蛋汤', 4.00, 1);
INSERT INTO `setmeal_dish` VALUES (67, 39, 55, '蒜蓉娃娃菜', 18.00, 1);
INSERT INTO `setmeal_dish` VALUES (68, 39, 65, '草鱼2斤', 68.00, 1);
INSERT INTO `setmeal_dish` VALUES (69, 39, 49, '米饭', 2.00, 1);
INSERT INTO `setmeal_dish` VALUES (70, 39, 47, '北冰洋', 4.00, 1);
INSERT INTO `setmeal_dish` VALUES (101, 40, 49, '米饭', 2.00, 20);
INSERT INTO `setmeal_dish` VALUES (107, 38, 46, '王老吉', 6.00, 1);
INSERT INTO `setmeal_dish` VALUES (108, 38, 63, '香锅牛蛙', 88.00, 1);
INSERT INTO `setmeal_dish` VALUES (109, 38, 68, '鸡蛋汤', 4.00, 1);
INSERT INTO `setmeal_dish` VALUES (110, 38, 54, '清炒小油菜', 18.00, 1);
INSERT INTO `setmeal_dish` VALUES (111, 38, 49, '米饭', 2.00, 1);

-- ----------------------------
-- Table structure for shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '商品名称',
  `image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '图片',
  `user_id` bigint NOT NULL COMMENT '主键',
  `dish_id` bigint NULL DEFAULT NULL COMMENT '菜品id',
  `setmeal_id` bigint NULL DEFAULT NULL COMMENT '套餐id',
  `dish_flavor` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '口味',
  `number` int NOT NULL DEFAULT 1 COMMENT '数量',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 116 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '购物车' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shopping_cart
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `openid` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '微信用户唯一标识',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '手机号',
  `sex` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '性别',
  `id_number` varchar(18) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '身份证号',
  `avatar` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '头像',
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '用户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (4, 'wx_openid_example_123456', '张三', NULL, '0', NULL, 'https://thirdwx.qlogo.cn/mmopen/vi_32/PiajxSqBRaEKgArwic4mcfErpFPoj06spS9gOib3DLybZuC62w0I4DUg60kia8ZJmdjhlvtD9LUt9iaxurbE22b9pIZO9sAGlLcNoZxxQicibqvGBzdE7oiaY4ibhicQ/132', '2026-01-17 18:28:15');
INSERT INTO `user` VALUES (6, NULL, '李四', NULL, NULL, NULL, NULL, '2026-01-21 18:28:15');
INSERT INTO `user` VALUES (7, NULL, '王五', NULL, NULL, NULL, NULL, '2026-01-21 18:28:15');

SET FOREIGN_KEY_CHECKS = 1;

