import { useCallback, useEffect, useMemo, useState } from 'react';
import { useAuth } from '../context/AuthProvider';
import { getHomeView } from '../services/homeService';
import AppShell from '../components/layout/AppShell';
import Header from '../components/layout/Header';
import MainContent from '../components/layout/MainContent';
import Sidebar from '../components/sidebar/Sidebar';
import MenuDrawer from '../components/menu/MenuDrawer';

const TOP_PICKS_COUNT = 8;

export default function Home() {
  const { user } = useAuth();
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [homeStatus, setHomeStatus] = useState('loading');
  const [trendingProducts, setTrendingProducts] = useState([]);
  const [savedItems, setSavedItems] = useState(null);
  const [viewAllSaved, setViewAllSaved] = useState(false);

  useEffect(() => {
    let isMounted = true;
    getHomeView({ page: 0, size: 20 })
      .then((data) => {
        if (!isMounted) return;
        setTrendingProducts(data.trendingProducts ?? []);
        const savedItemsContent = data.savedItemDtos?.content ?? [];
        setSavedItems(
          savedItemsContent.length > 0
            ? savedItemsContent.map((item) => ({ ...item, createdAt: item.addedAt }))
            : null,
        );
        setHomeStatus('success');
      })
      .catch(() => {
        if (!isMounted) return;
        setHomeStatus('error');
      });
    return () => { isMounted = false; };
  }, []);

  const handleSearch = useCallback((query) => setSearchQuery(query), []);
  const handleToggleViewAllSaved = useCallback(() => setViewAllSaved((prev) => !prev), []);

  const topPicks = useMemo(
    () => trendingProducts.slice(0, TOP_PICKS_COUNT),
    [trendingProducts],
  );

  const hotPicks = useMemo(() => {
    const rest = trendingProducts.slice(TOP_PICKS_COUNT);
    if (!searchQuery.trim()) return rest;
    const query = searchQuery.trim().toLowerCase();
    return rest.filter((product) => product.title?.toLowerCase().includes(query));
  }, [trendingProducts, searchQuery]);

  const savedItemsPreview = useMemo(
    () => (savedItems ? savedItems.slice(0, TOP_PICKS_COUNT) : null),
    [savedItems],
  );

  return (
    <>
      <AppShell
        header={
          <Header
            onToggleMenu={() => setDrawerOpen((open) => !open)}
            onSearch={handleSearch}
            isMenuOpen={drawerOpen}
          />
        }
        sidebar={<Sidebar isBuyer={user.isBuyer} isSupplier={user.isSupplier} />}
        main={
          <MainContent
            topPicks={topPicks}
            hotPicks={hotPicks}
            status={homeStatus}
            savedItems={savedItems}
            savedItemsPreview={savedItemsPreview}
            savedItemsStatus={homeStatus}
            viewAllSaved={viewAllSaved}
            onToggleViewAllSaved={handleToggleViewAllSaved}
          />
        }
      />
      <MenuDrawer open={drawerOpen} onClose={() => setDrawerOpen(false)} user={user} />
    </>
  );
}
