<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/css/libs/datepicker.css" type="text/css" />
    </s:layout-component>
    <s:layout-component name="page_css_inline">

    </s:layout-component>
    <s:layout-component name="page_container">
        <div class="col-lg-12">
            <h1>Approval for Hardware Request</h1>
            <div class="row">
                <div class="col-lg-6">
                    <div class="main-box">
                        <h2>Hardware Request Information</h2>
                        <form id="approval_hardwarequest_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRequest/approvalupdate" method="post">
                            <input type="hidden" name="id" value="${whRequest.id}" />
                            <div class="form-group">
                                <label for="requestBy" class="col-lg-4 control-label">Request By</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="requestBy" name="requestBy" placeholder="Request For" value="${whRequest.requestedBy}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="materialPassNo" class="col-lg-4 control-label">Material Pass No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassNo" name="materialPassNo" value="${whRequest.materialPassNo}" readonly>
                                </div>
                            </div>
                                <div class="form-group">
                                <label for="materialPassExpiry" class="col-lg-4 control-label">Material Pass Expiry Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassExpiry" name="materialPassExpiry" value="${whRequest.materialPassExpiry}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentType" class="col-lg-4 control-label">Hardware Category</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentType" name="equipmentType" value="${whRequest.equipmentType}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentId" class="col-lg-4 control-label">Equipment ID</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentId" name="equipmentId" value="${whRequest.equipmentId}" readonly>
                                </div>
                            </div>
                            <div class="form-group" id="typediv" hidden>
                                <label for="type" class="col-lg-4 control-label">Type </label>
                                <div class="col-lg-6">
                                    <input type="text" class="form-control" id="type" name="type" placeholder="Type" value="${whRequest.type}" readonly>
                                </div>
                            </div>    
                            <div class="form-group" id="quantitydiv" hidden>
                                <label for="quantity" class="col-lg-4 control-label">Quantity *</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="quantity" name="quantity" placeholder="Quantity" value="${whRequest.quantity}" readonly>
                                </div>
                            </div> 
                            <div class="form-group">
                                <label for="rack" class="col-lg-4 control-label">Rack</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="rack" name="rack" value="${whRequest.rack}" readonly>
                                </div>
                            </div> 
                            <div class="form-group">
                                <label for="slot" class="col-lg-4 control-label">Slot</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="slot" name="slot" value="${whRequest.slot}" readonly>
                                </div>
                            </div>  
                            <div class="form-group" id="remarksDiv" >
                                <label for="remarks" class="col-lg-4 control-label">Remarks </label>
                                <div class="col-lg-8">
                                    <textarea class="form-control" rows="5" id="remarks" name="remarks" readonly>${whRequest.remarks}</textarea>
                                </div>
                            </div>
                            <a href="${contextPath}/wh/whRequest" class="btn btn-info pull-left"><i class="fa fa-reply"></i> Back</a>
                            <!--<div class="pull-right">
                                <!--button type="reset" class="btn btn-secondary cancel">Reset</button>
                                <button type="submit" class="btn btn-primary">Save</button>
                            </div>-->
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>	
            </div>
        </div>
                            
        <!--New Tab Menu-->
       <hr class="separator">
        <div class="col-lg-12">
            <br>
            <div class="row">
                <ul class="nav nav-tabs">
                    <li class="${mpActive}"><a data-toggle="tab" href="#mp">Verify Barcode Label</a></li>
                    <!--li class="${hiActive}"><a data-toggle="tab" href="#hi">Hardware Inventory</a></li-->
                </ul>
                <div class="tab-content">

                    <!--Tab for Scan Material Pass-->
                    <div id="mp" class="tab-pane fade ${mpActiveTab}">

                        <h6></h6>
                        <div class="col-lg-6">
                            <div class="main-box">
                                <h2>Verify Barcode Label</h2>
                                <form id="mp_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRequest/verifyMp" method="post">
                                    <input type="hidden" name="refId" value="${whRequest.refId}" />
                                    <input type="hidden" name="status" value="${whRequest.status}" />
                                    <input type="hidden" name="refId" value="${mpActiveTab}" />
                                    <div class="form-group">
                                        <label for=" materialPassNo" class="col-lg-4 control-label">Scan Barcode Label *</label>
                                        <div class="col-lg-8">
                                            <input type="text" class="form-control" id="materialPassNo" name="materialPassNo" autofocus="autofocus" />
                                        </div>
                                    </div>
                                    <br><br>
                                    <div class="pull-right">
                                        <button type="reset" class="btn btn-secondary cancel">Reset</button>
                                        <button type="submit" class="btn btn-primary">Verify</button>
                                    </div>
                                    <div class="clearfix"></div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!--tab for scan trip ticket
                    <div id="hi" class="tab-pane fade ${hiActiveTab}">
                        <h6></h6>
                        <div class="col-lg-6">
                            <div class="main-box">
                                <h2>Inventory Locator</h2>
                                <form id="hi_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRequest/whInventory" method="post">
                                    <input type="hidden" name="refId" value="${whRequest.refId}" />
                                    <input type="hidden" name="status" value="${whRequest.status}" />
                                    <input type="hidden" name="refId" value="${hiActiveTab}" />
                                    <label for=" rack" class="col-lg-4 control-label">Rack Number</label>
                                    <div class="col-lg-8">
                                        <input type="text" class="form-control" id="rack" name="rack" autofocus="autofocus" />
                                    </div>
                                    <br><br>
                                    <label for=" slot" class="col-lg-4 control-label">Slot Number</label>
                                    <div class="col-lg-8">
                                        <input type="text" class="form-control" id="slot" name="slot" autofocus="autofocus" />
                                    </div>
                                    <br><br><br>
                                    <div class="pull-right">
                                        <button type="reset" class="btn btn-secondary cancel1">Reset</button>
                                        <button type="submit" class="btn btn-primary">Save</button>
                                    </div>
                                    <div class="clearfix"></div>
                                </form>
                            </div>
                        </div>
                    </div>-->
                </div>
            </div>
        </div>
        <!--END tab menu-->
        
    </s:layout-component>
    <s:layout-component name="page_js">
        <script src="${contextPath}/resources/validation/jquery.validate.min.js"></script>
        <script src="${contextPath}/resources/validation/additional-methods.js"></script>
        <script src="${contextPath}/resources/private/js/bootstrap-datepicker.js"></script>
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
            $(document).ready(function () {

                var element = $('#equipmentType');
                if (element.val() === "Motherboard") {
                    $("#typediv").hide();
                    $("#quantitydiv").hide();
                } else if (element.val() === "Stencil") {
                    $("#typediv").hide();
                    $("#quantitydiv").hide();
                } else if (element.val() === "Tray") {
                    $("#quantitydiv").show();
                    $("#typediv").hide();
                } else if (element.val() === "PCB") {
                    $("#typediv").hide();
                    $("#quantitydiv").show();
                } else {
                    $("#typediv").hide();
                    $("#quantitydiv").hide();
                }


                var validator = $("#approval_hardwarequest_form").validate({
                    rules: {
                        finalApprovedStatus: {
                            required: true
                        }
                    }
                });
                $(".cancel").click(function () {
                    validator.resetForm();
                });
            });


        </script>
    </s:layout-component>
</s:layout-render>