<%-- 
    Document   : list_ready
    Created on : Jun 21, 2023, 5:10:23 PM
    Author     : zbqb9x
--%>

<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/jquery.dataTables.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/buttons.dataTables.min.css" type="text/css" />
    </s:layout-component>
    <s:layout-component name="page_css_inline">
        <style>
        </style>
    </s:layout-component>
    <s:layout-component name="page_container">
        <div class="col-lg-12">
            <h1>WIP Management - TO BE READY</h1>
            <div class="row">
                <div class="col-lg-12">
                    <div class="main-box">
                        <h2>Scan GTS Number</h2>
                        <form id="shipping_form" class="form-horizontal" role="form" action="${contextPath}/whWip/updateReadyToShip" method="post">
                            <div class="form-group">
                                <label for="shipDate" class="col-lg-2 control-label">Shipping Date Time *</label>
                                <div class="col-lg-12">
                                    <input type="datetime-local" id="shipDate" name="shipDate" class="btn pull-right">
                                    <!--<input type="text" class="form-control" id="boxNo" name="boxNo" placeholder="" value="" autofocus="autofocus">-->
                                </div>
                            </div>
                            <a href="${contextPath}/whWip/to" class="btn btn-info pull-left"><i class="fa fa-reply"></i> Back</a>
                            <div class="pull-right">
                                <button type="submit" class="btn btn-primary">PRINT for ${wipBox}</button>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>	
            </div>
        </div>
        <div class="col-lg-12">
            <!--<h1>WIP Management - TO BE READY</h1>-->
            <div class="row">
                <div class="col-lg-12">
                    <div class="main-box clearfix">
<!--                            <div>
                                <h2 class="pull-left">PAGE WIP TO BE READY</h2>
                                <c:if test="${checkReady > 0}">
                                    <div class="filter-block pull-right">
                                        <a href="${contextPath}/whWip/updateReadyToShip" class="btn btn-primary pull-right">
                                            <i class="fa fa-pencil-square-o fa-lg"></i> PRINT for ${wipBox}
                                        </a>
                                    </div>
                                    <div class="filter-block pull-right"></div>
                                </c:if>
                                <div class="form-group pull-right">
                                    <div class="form-group pull-right" style="margin-right: 0px;">
                                        <input type="datetime-local" id="shipDate" name="shipDate" class="btn pull-right">`
                                    </div>
                                </div>
                            </div>-->
                            <hr/>
                            <div class="clearfix">
                                <div class="form-group pull-left">
                                    <select id="dt_spml_rows" class="form-control">
                                        <option value="10">10</option>
                                        <option value="25">25</option>
                                        <option value="50">50</option>
                                        <option value="100">100</option>
                                    </select>
                                </div>
<!--                                <div class="form-group pull-right">
                                    <div class="form-group pull-right" style="margin-right: 0px;">
                                        <input type="datetime-local" id="shipDate" name="shipDate" class="btn pull-right">`
                                    </div>
                                </div>-->
                            </div>
                            <div class="table-responsive">
                                <table id="dt_spml" class="table">
                                    <thead>
                                        <tr>
                                            <th><span>Request ID</span></th> 
                                            <th><span>GTS NO</span></th>
                                            <th><span>RMS Event</span></th>
                                            <th><span>Intervals</span></th>
                                            <th><span>Quantity</span></th>
                                            <th><span>Shipment Date</span></th>
                                            <th><span>Status</span></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${wipList}" var="whWip" varStatus="whWipLoop">
                                            <tr>
                                                <td><c:out value="${whWip.requestId}"/></td>
                                                <td><c:out value="${whWip.gtsNo}"/></td>
                                                <td><c:out value="${whWip.rmsEvent}"/></td>
                                                <td><c:out value="${whWip.intervals}"/></td>
                                                <td><c:out value="${whWip.quantity}"/></td>
                                                <td><c:out value="${whWip.shipmentDate}"/></td>
                                                <td><c:out value="${whWip.status}"/></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                                <!--<div><a href="${contextPath}/whWip/to" class="btn btn-info pull-left"><i class="fa fa-reply"></i> Back</a></div>-->
                            </div>
                    </div>
                </div>
            </div>
        </div>
        <hr class="separator">
    </s:layout-component>
    <s:layout-component name="page_js">
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
                var validator = $("#shipping_form").validate({
                    rules: {
                        shipDate: {
                            required: true
                        }
                    }
                });
            });
        </script>
    </s:layout-component>
</s:layout-render>