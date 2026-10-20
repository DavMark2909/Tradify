import { useCallback, useMemo, useState } from 'react';
import { useAuth } from '../context/AuthProvider';
import { useItems } from '../hooks/useItems';
import ItemsShell from '../components/layout/ItemsShell';
import Header from '../components/layout/Header';
import Sidebar from '../components/sidebar/Sidebar';
import ItemsContent from '../components/layout/ItemsContent';
import MenuDrawer from '../components/menu/MenuDrawer';

export default function Items({ type }) {
  const { user } = useAuth();
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const { items, status } = useItems(type);

  const handleSearch = useCallback((query) => setSearchQuery(query), []);

  const filteredItems = useMemo(() => {
    const query = searchQuery.trim().toLowerCase();
    if (!query) return items;
    return items.filter((item) => item.title?.toLowerCase().includes(query));
  }, [items, searchQuery]);

  return (
    <>
      <ItemsShell
        header={
          <Header
            onToggleMenu={() => setDrawerOpen((open) => !open)}
            onSearch={handleSearch}
            isMenuOpen={drawerOpen}
          />
        }
        sidebar={<Sidebar isBuyer={user.isBuyer} isSupplier={user.isSupplier} />}
        main={<ItemsContent type={type} items={filteredItems} status={status} />}
      />
      <MenuDrawer open={drawerOpen} onClose={() => setDrawerOpen(false)} user={user} />
    </>
  );
}
