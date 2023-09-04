<%-- 
    Document   : wip_load
    Created on : Jul 31, 2023, 10:28:08 AM
    Author     : zbqb9x
--%>

<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/jquery.dataTables.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/buttons.dataTables.min.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/css/005.css" type="text/css" />
    </s:layout-component>
    <s:layout-component name="page_css_inline">
        <style>
            @media print {
                table thead {
                    border-top: #000 solid 2px;
                    border-bottom: #000 solid 2px;
                }
                table tbody {
                    border-top: #000 solid 2px;
                    border-bottom: #000 solid 2px;
                }
            }
            .dataTables_wrapper .dt-buttons {
                float:none;  
                text-align:right;
            }
        </style>
    </s:layout-component>
        <s:layout-component name="page_container">
        <div class="col-lg-12">
            <h1>WIP Management [Stress WIP]</h1>
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box">
                        <h2>LOAD STRESS WIP</h2>
                        <form id="wip_process_form" class="form-horizontal" role="form" action="${contextPath}/whWip/updateProcess/loading" method="post">
                            <input type="hidden" name="requestId" value="${wipData.requestId}" />
                            <div class="form-group">
                                <label for="gtsNo" class="col-lg-3 control-label">GTS No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="gtsNo" name="gtsNo" value="${wipData.gtsNo}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="rms" class="col-lg-3 control-label">RMS Event No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="rms" name="rms" value="${wipData.rmsEvent}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="intervals" class="col-lg-3 control-label">Intervals</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="intervals" name="intervals" value="${wipData.intervals}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="quantity" class="col-lg-3 control-label">Quantity</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="quantity" name="quantity" value="${wipData.quantity}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="shipDate" class="col-lg-3 control-label">Shipment Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="shipDate" name="shipDate" value="${wipData.shipmentDate}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="loadDate" class="col-lg-3 control-label">Loading Date</label>
                                <div class="col-lg-5">
                                    <input type="datetime-local" class="form-control" id="loadDate" name="loadDate" >
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="chamber" class="col-lg-3 control-label">Chamber</label>
                                <div class="col-lg-5">
                                    <select id="chamber" name="chamber" class="js-example-basic-single" style="width: 100%" >
                                        <option value="" selected=""></option>
                                        <c:forEach items="${chamber}" var="list">
                                            <option value="${list.name}"> ${list.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <a href="${contextPath}/whWip/listProcess" class="btn btn-info pull-left" style="font-family:'Orbitron', monospace;"><i class='bx bxs-chevron-left bx-fw'></i> Back</a>
                            <div class="pull-right">
                                <button type="submit" class="btn btn-primary" style="font-family:'Orbitron', monospace;"><i class='bx bx-archive-in bc-fw bx-md' ></i> Load WIP</button>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>	
            </div>
        </div>
    </s:layout-component>
    <s:layout-component name="page_js">
        <!--print-->
        <script src="${contextPath}/resources/private/datatables/js/jquery.dataTables.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/dataTables.buttons.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.print.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.flash.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.html5.min.js"></script>
        <script src="${contextPath}/resources/validation\jquery.validate.js"></script>
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
            $(document).ready(function () {
                var validator = $("#wip_process_form").validate({
                    rules: {
                        loadDate: {
                            required: true
                        }
                    }
                });
            });
        </script>
    </s:layout-component>
</s:layout-render>