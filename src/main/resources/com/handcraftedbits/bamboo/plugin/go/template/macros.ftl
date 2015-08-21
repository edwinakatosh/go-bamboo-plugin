[#macro goRootAndSourcePath]
  [@s.textfield name="goroot" labelKey="task.common.field.goroot" cssClass="long-field" required=true /]
  [@s.textfield name="sourcePath" labelKey="task.common.field.sourcePath" cssClass="long-field" required=true /]
[/#macro]

[#macro package index=0 packageName="" flags="" hasAddButton=false]
  <tr data-package-index="${index}">
    <td class="gbp-widget-packages-packageName">
      [@s.textfield name="_gbp_packageName_${index}" value="${packageName}" required=true /]
    </td>
    <td class="gbp-widget-packages-flags">
      [@s.textfield name="_gbp_flags_${index}" value="${flags}" /]
    </td>
    <td>
      <div class="field-group">
        <div class="aui-button aui-button-subtle">
          [#if hasAddButton]
            <span class="aui-icon aui-icon-small aui-iconfont-list-add">
              [@s.text name="task.common.button.add" /]
            </span>
          [#else]
            <span class="aui-icon aui-icon-small aui-iconfont-list-remove">
              [@s.text name="task.common.button.remove" /]
            </span>
          [/#if]
        </div>
      </div>
    </td>
  </tr>
[/#macro]

[#macro packagesSection sectionKey=""]
  [@ui.bambooSection id="sectionPackages" titleKey="${sectionKey}" collapsible=true isCollapsed=false]
    <input type="hidden" name="_gbp_indices" id="_gbp_indices" value=""></input>
    <table class="aui">
      <thead>
        <tr>
          <th>
            [@s.text name="task.common.field.package" /]<span class="aui-icon icon-required"></span>
          </th>
          <th>[@s.text name="task.common.field.flags" /]</th>
          <th></th>
        </tr>
        <tbody id="_gbp_packagesBody">
          [#list _gbp_packages as pkg]
            [@package pkg.index pkg.name pkg.flags pkg.last /]
          [/#list]
        </tbody>
      </thead>
    </table>
  [/@ui.bambooSection]

  <script type="text/x-template" title="gbp-widget-package-error-template">
    <div class="error gbp-package-error">
      [@s.text name="task.common.field.package.error" /]
    </div>
  </script>

  <script type="text/x-template" title="gbp-widget-packages-template">
    [#assign packageInstance]
      [@package 9999 "" "" true /]
    [/#assign]
    ${packageInstance?replace("9999", "{index}")}
  </script>
[/#macro]