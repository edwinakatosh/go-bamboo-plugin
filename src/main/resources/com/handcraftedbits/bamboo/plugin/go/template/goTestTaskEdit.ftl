[#include "macros.ftl"]

[@goRootAndSourcePath /]

[@packagesSection sectionKey="task.build.section.packages" /]

[@s.textfield name="logOutputPath" labelKey="task.test.field.logOutputPath" cssClass="long-field" required=true /]
[@s.checkbox name="logOutputToBuild" labelKey="task.test.field.logOutputToBuild" /]

[@ui.bambooSection id="sectionAdvancedOptions" titleKey="repository.advanced.option" collapsible=true
  isCollapsed=!(environmentVariables?has_content)]
  [@s.textfield name="environmentVariables" labelKey="builder.common.env" cssClass="long-field" /]
[/@ui.bambooSection]

<script type="text/javascript">
  BAMBOO.GBP.GoTaskConfiguration.init();
</script>
