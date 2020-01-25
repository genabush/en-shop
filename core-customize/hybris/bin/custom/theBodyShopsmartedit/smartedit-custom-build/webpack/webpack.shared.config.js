/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
const {
    resolve
} = require('path');

const {
    group,
    webpack: {
        entry,
        alias
    }
} = require('../../smartedit-build/builders');

const commonsAlias = alias('theBodyShopsmarteditcommons', resolve('./jsTarget/web/features/theBodyShopsmarteditcommons'));

const smartedit = group(
    commonsAlias,
    alias('theBodyShopsmartedit', resolve('./jsTarget/web/features/theBodyShopsmartedit'))
);
const smarteditContainer = group(
    commonsAlias,
    alias('theBodyShopsmarteditcontainer', resolve('./jsTarget/web/features/theBodyShopsmarteditContainer')),
);

module.exports = {
    ySmarteditKarma: () => group(
        smartedit
    ),
    ySmarteditContainerKarma: () => group(
        smarteditContainer
    ),
    ySmartedit: () => group(
        smartedit,
        entry({
            theBodyShopsmartedit: resolve('./jsTarget/web/features/theBodyShopsmartedit/theBodyShopsmarteditModule.ts')
        })
    ),
    ySmarteditContainer: () => group(
        smarteditContainer,
        entry({
            theBodyShopsmarteditContainer: resolve('./jsTarget/web/features/theBodyShopsmarteditContainer/theBodyShopsmarteditcontainerModule.ts')
        })
    )
};
