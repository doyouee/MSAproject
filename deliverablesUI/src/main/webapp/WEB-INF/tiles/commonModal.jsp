<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<!-- Complete Modal -->
<div id="completeModal" class="modal fade" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-dialog-centered modal-dialog-alert">
		<div class="modal-content" style="width: 100%">
			<div class="modal-body">
				<i class="iconb-compt"></i>
				<p class="alert-text" id="completeModalContent"></p>
			</div>
			<div class="modal-footer">
				<button id="completeModalButton" type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
			</div>
		</div>
	</div>
</div>

<!-- Fail Modal -->
<div id="failModal" class="modal fade" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-dialog-centered modal-dialog-alert">
		<div class="modal-content" style="width: 100%">
			<div class="modal-body">
				<i class="iconb-danger"></i>
				<p class="alert-text" id="failModalContent"></p>
			</div>
			<div class="modal-footer">
                <button id="failModalCancelButton" type="button" class="btn" data-dismiss="modal" style="display: none">취소</button>
				<button id="failModalButton" type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
			</div>
		</div>
	</div>
</div>

<!-- Warn Modal -->

<div id="warnModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered modal-dialog-alert">
        <div class="modal-content" style="width: 100%">
            <div class="modal-body">
                <i class="iconb-warn"></i>
                <p class="alert-text" id="warnModalContent" ></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn" data-dismiss="modal">취소</button>
                <button id="warnModalButton" type="button" class="btn btn-primary"  name="confirmBtn" data-dismiss="modal">확인</button>
            </div>
        </div>
    </div>
</div>

<!-- Download Modal -->
<div id="downloadModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered modal-dialog-alert">
        <div class="modal-content" style="width: 100%">
            <div class="modal-body">
                <i class="iconb-warn"></i>
                <p class="alert-text" id="downloadModalContent" ></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn" data-dismiss="modal">취소</button>
                <button id="downloadModalButton" type="button" class="btn btn-primary"   data-dismiss="modal">확인</button>
            </div>
        </div>
    </div>
</div>

<%--상세조회 모달--%>
<form id="detailModalForm">
<div id="detailModal" class="modal fade" tabindex="-1" role="dialog" style="display: none;"
     aria-modal="true">
    <div class="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <div class="modal-content" style="width:50%;">
            <div class="modal-header">
                <h2 class="modal-title" id = "detailModalHeader">사용자 상세조회</h2>
                <button type="button" class="btn-icon" data-dismiss="modal" aria-label="Close"><i
                        class="icon-close"></i></button>
            </div>
            <div class="table-responsive jusify-content-center"
                 style="padding-top: 2%; padding-bottom: 2%; padding-left:4%; padding-right:4%;">
                <table style="padding: 2px;" class="table" id="tableTest">
                    <colgroup class="jusify-content-center">
                        <col class="col" style="width: 30%">
                        <col class="col" style="width: 70%">
                    </colgroup>
                    <tbody>
                    <tr>
                        <td class="alert-text">이름</td>
                        <td id="searchUserName" style="text-align: left"></td>
                    </tr>
                    <tr>
                        <td class="alert-text">이메일</td>
                        <td id="searchUserEmail" style="text-align: left"></td>
                    </tr>
                    <tr>
                        <td class="alert-text" >부서명</td>
                        <td id="searchTeamName" style="text-align: left"></td>
                    </tr>
                    <tr>
                        <td class="alert-text">권한</td>
                        <td id="searchAuthorityId" style="text-align: left"></td>
                    </tr>

                    <tr style="display:none" id="detailModalPwEdit">
                        <td class="alert-text">비밀번호 변경</td>
                        <td class="alert-text" style="text-align: left"><button type="button" id="pwModalBtn" class="btn btn-write">변경</button></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</form>

<%--비밀번호 모달--%>
<form id="pwModalForm">
    <div>
    <div id="pwModal" class="modal fade" tabindex="-1" role="dialog" style="display: none;" aria-modal="true">
        <div class="modal-dialog modal-dialog-centered modal-sm">
            <div class="modal-content" style="width:100%;height:50%">
                <div class="modal-header">
                    <h2 class="modal-title">비밀번호 변경</h2>
                    <button type="button" class="btn-icon" data-dismiss="modal" aria-label="Close"><i
                            class="icon-close"></i></button>
                </div>
                <div class="modal-body">
                    <div >
                        <label class="control-label">이전 비밀번호</label>
                        <div style="position: relative;margin-bottom: 10px; text-align:left;">
                            <input id="pwModalBeforePw" name="pwModalBeforePw" type="password" class="form-control"
                                   required=""
                                   placeholder="이전 비밀번호를 입력해 주세요." autocomplete="on">
                            <span class="icon-x" style="color:red; font-size:12px; display:none"
                                  id="pwModalBeforePwValidation">&nbsp;값이 존재하지 않습니다.</span>
                        </div>
                    </div>

                    <div >
                        <label class="control-label">비밀번호</label>
                        <div style="position: relative;margin-bottom: 10px; text-align:left;">
                            <input id="pwModalNewPw" name="pwModalNewPw" type="password" class="form-control"
                                   required=""
                                   placeholder="새로운 비밀번호를 입력해 주세요." autocomplete="on">
                            <span class="icon-x" style="color:red; font-size:12px; display:none"
                                  id="pwModalNewPwValidation">&nbsp;값이 존재하지 않습니다.</span>
                        </div>
                    </div>
                    <div>
                        <label class="control-label">비밀번호 확인</label>
                        <div style="position: relative;margin-bottom: 10px; text-align:left;">
                            <input id="pwModalNewPwConfirm" name="pwModalNewPwConfirm" type="password"
                                   class="form-control"
                                   required=""
                                   placeholder="동일한 비밀번호를 입력해 주세요." autocomplete="on">
                            <span class="icon-x" style="color:red; font-size:12px; display:none"
                                  id="pwModalNewPwConfirmValidation">&nbsp;값이 존재하지 않습니다.</span>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn" data-dismiss="modal">취소</button>
                    <button id="pwChangeBtn" type="button" class="btn btn-primary">변경</button>
                </div>
            </div>
        </div>
    </div>
    </div>
</form>
