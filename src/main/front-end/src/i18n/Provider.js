import React, {Fragment} from 'react';
import { IntlProvider } from 'react-intl';
import messages from './messages';
import {getLanguage} from "../services/UserDataService";

const Provider = ({children, locale = getLanguage()}) => (
  <IntlProvider
  locale={locale}
  textComponent={Fragment}
  messages={messages[locale]}>
    {children}
  </IntlProvider>
);

export default Provider;