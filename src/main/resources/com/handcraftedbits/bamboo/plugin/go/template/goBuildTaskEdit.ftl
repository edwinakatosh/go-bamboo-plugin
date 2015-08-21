[#include "macros.ftl"]

[@goRootAndSourcePath /]

[@packagesSection sectionKey="task.build.section.packages" /]

[@ui.bambooSection id="sectionAdvancedOptions" titleKey="repository.advanced.option" collapsible=true
  isCollapsed=!(environmentVariables?has_content)]
  [@s.textfield name="environmentVariables" labelKey="builder.common.env" cssClass="long-field" /]
[/@ui.bambooSection]

<script type="text/javascript">
  BAMBOO.GBP.GoTaskConfiguration.init();
</script>