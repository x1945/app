<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="random" value="<%=Math.random()%>" />

<body>
	<div class="demo-layout mdl-layout mdl-layout--fixed-header mdl-js-layout mdl-color--grey-100">
		<header class="demo-header mdl-layout__header mdl-layout__header--scroll mdl-color--grey-100 mdl-color-text--grey-800">
			<div class="mdl-layout__header-row">
				<span class="mdl-layout-title">全文檢索Demo</span>
				<div class="mdl-layout-spacer"></div>
				<div class="mdl-textfield mdl-js-textfield mdl-textfield--expandable">
					<label class="mdl-button mdl-js-button mdl-button--icon" for="search"> <i class="material-icons">search</i>
					</label>
					<div class="mdl-textfield__expandable-holder">
						<input class="mdl-textfield__input" type="text" id="search" onkeypress="keyEvent(event)"> <label class="mdl-textfield__label" for="search">Enter your query...</label>
					</div>
				</div>
			</div>
		</header>
		<div class="demo-ribbon"></div>
		<main class="demo-main mdl-layout__content">
		<div class="demo-container mdl-grid">
			<div class="mdl-cell mdl-cell--2-col mdl-cell--hide-tablet mdl-cell--hide-phone"></div>
			<div class="demo-content mdl-color--white mdl-shadow--4dp content mdl-color-text--grey-800 mdl-cell mdl-cell--8-col">
<!-- 				<h3>文章</h3> -->
<!-- 				<hr> -->
<!-- 				<ul> -->
<!-- 					<li>1.曾經有一份真摯的感情放在我面前.我沒有珍惜.等到失去的時候才後悔莫及，塵世間最痛苦的事莫過於此.你的劍在我的咽喉上割下去吧!不要再猶豫了!如果上天能夠給我一個再來一次的機會，我一定會對那個女孩子說三個字\"我愛你\",如果非要在這份愛上加一個期限的話，我希望是一萬年。</li> -->
<!-- 					<li>2.本年度計畫以前期計畫發展配方為基礎，使用可代謝caprylic/capric glycerides為油相，製成water-in-polymer -->
<!-- 						油包水型佐劑，期能改良前期佐劑效價。佐劑穩定性之評估結果顯示油、水比7:3之WP-b佐劑具有良好乳化穩定性且通過小鼠急毒性試驗。佐劑效力評估使用SPF雞隻為目標動物及以不活化新城雞瘟病毒為抗原，佐劑與抗原混和後進行雞隻免疫試驗，後採取血清分析其HI抗體力價。結果顯示混合WP-b佐劑組別的雞隻在免疫後兩週、四週 -->
<!-- 						其HI抗體力價皆顯著高於前期開發之WE佐劑。經儲存10個月後，其誘導HI抗體佐劑效力並未衰退。無論是一次免疫注射或是二次免疫注射結果皆顯示第十週血清抗體呈陽性反應。我們所開發之WP-b佐劑有潛力成為新一代動物疫苗用佐劑。</li> -->
<!-- 					<li>3.菸鹼醯胺腺嘌呤二核苷酸磷酸葡萄糖水解酶。</li> -->
<!-- 				</ul> -->
<!-- 				<hr> -->
				<h3>查詢結果 共<span id="count">0</span>筆，查詢時間<span id="searchTime">0</span>秒</h3>
				<hr>
				<div id="searchReslut"></div>
			</div>
		</div>
		</main>
	</div>
	<script src="<c:url value='/views/es.js' />?r=${random}"></script>
</body>
</html>