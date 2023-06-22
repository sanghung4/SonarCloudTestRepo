import React from 'react';

import {
  Box,
  Grid,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Typography
} from '@dialexa/reece-component-library';

import { SavePdfIcon } from 'icons';
import { t } from 'i18next';
import { JobFormLinkContainer } from './utils/styled';
import { testIds } from 'test-utils/testIds';

const fortilineFormURLS = {
  url: 'Job_Information_Sheet.pdf',
  title: 'Job Form Application'
};

function JobFormLink() {
  return (
    <JobFormLinkContainer data-testid={testIds.JobForm.JobFormLink.page}>
      <Grid container>
        <Typography variant="body1" display="inline" marginBottom={2}>
          {t('jobForm.linkInfo')}
        </Typography>
        <Grid item xs={12} md={4}>
          <List>
            <ListItem
              key={fortilineFormURLS.title}
              component="a"
              href={`/files/${fortilineFormURLS.url}`}
              target="_blank"
              disableGutters
            >
              <ListItemIcon>
                <SavePdfIcon />
              </ListItemIcon>
              <ListItemText
                primary={
                  <Box component="span" color="primary02.main">
                    {fortilineFormURLS.title}
                  </Box>
                }
              />
            </ListItem>
          </List>
        </Grid>
      </Grid>
    </JobFormLinkContainer>
  );
}
export default JobFormLink;
